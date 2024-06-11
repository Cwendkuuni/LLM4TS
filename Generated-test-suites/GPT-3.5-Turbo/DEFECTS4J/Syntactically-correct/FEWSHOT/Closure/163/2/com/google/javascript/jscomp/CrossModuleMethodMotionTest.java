import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
    private static final String STUB_METHOD_NAME = "JSCompiler_stubMethod";
    private static final String UNSTUB_METHOD_NAME = "JSCompiler_unstubMethod";

    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private AbstractCompiler compiler;
    private JSModuleGraph moduleGraph;
    private AnalyzePrototypeProperties analyzer;
    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setup() {
        idGenerator = mock(CrossModuleMethodMotion.IdGenerator.class);
        compiler = mock(AbstractCompiler.class);
        moduleGraph = mock(JSModuleGraph.class);
        analyzer = mock(AnalyzePrototypeProperties.class);

        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, false);
        crossModuleMethodMotion.moduleGraph = moduleGraph;
        crossModuleMethodMotion.analyzer = analyzer;
    }

    @Test
    public void testProcess_ReturnsEarlyIfModuleCountIsLessThan2() {
        when(moduleGraph.getModuleCount()).thenReturn(1);

        crossModuleMethodMotion.process(null, null);

        verifyZeroInteractions(analyzer);
    }

    @Test
    public void testProcess_CallsAnalyzerProcessMethod() {
        when(moduleGraph.getModuleCount()).thenReturn(2);

        crossModuleMethodMotion.process(null, null);

        verify(analyzer, times(1)).process(any(Node.class), any(Node.class));
    }

    @Test
    public void testMoveMethods_DoesNotMoveUnreferencedNameInfos() {
        CrossModuleMethodMotion.NameInfo unreferencedNameInfo = mock(CrossModuleMethodMotion.NameInfo.class);
        when(unreferencedNameInfo.isReferenced()).thenReturn(false);

        crossModuleMethodMotion.moveMethods(Collections.singletonList(unreferencedNameInfo));

        verifyZeroInteractions(unreferencedNameInfo);
    }

    @Test
    public void testMoveMethods_DoesNotMoveNameInfosThatReadClosureVariables() {
        CrossModuleMethodMotion.NameInfo nameInfo = mock(CrossModuleMethodMotion.NameInfo.class);
        when(nameInfo.isReferenced()).thenReturn(true);
        when(nameInfo.readsClosureVariables()).thenReturn(true);

        crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

        verifyZeroInteractions(nameInfo);
    }

    @Test
    public void testMoveMethods_IgnoresNameInfoWithNullDeepestCommonModuleRefAndReportsError() {
        CrossModuleMethodMotion.NameInfo nameInfo = mock(CrossModuleMethodMotion.NameInfo.class);
        when(nameInfo.isReferenced()).thenReturn(true);
        when(nameInfo.readsClosureVariables()).thenReturn(false);
        when(nameInfo.getDeepestCommonModuleRef()).thenReturn(null);

        crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

        verify(compiler, times(1)).report(any(JSError.class));
    }

    @Test
    public void testMoveMethods_StubMethodIsGeneratedIfMethodCanBeMovedIntoDeeperModule() {
        CrossModuleMethodMotion.NameInfo nameInfo = mock(CrossModuleMethodMotion.NameInfo.class);
        when(nameInfo.isReferenced()).thenReturn(true);
        when(nameInfo.readsClosureVariables()).thenReturn(false);

        CrossModuleMethodMotion.Symbol symbol = mock(CrossModuleMethodMotion.Property.class);
        when(symbol instanceof CrossModuleMethodMotion.Property).thenReturn(true);
        when(nameInfo.getDeepestCommonModuleRef()).thenReturn(mock(JSModule.class));
        when(nameInfo.getDeclarations()).thenReturn(Collections.singletonList(symbol));

        when(symbol.getValue()).thenReturn(mock(Node.class));
        when(symbol.getPrototype()).thenReturn(mock(Node.class));

        when(moduleGraph.dependsOn(any(JSModule.class), any(JSModule.class))).thenReturn(true);
        when(idGenerator.newId()).thenReturn(1);

        crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testMoveMethods_StubMethodIsNotGeneratedIfMethodCannotBeMovedIntoDeeperModule() {
        CrossModuleMethodMotion.NameInfo nameInfo = mock(CrossModuleMethodMotion.NameInfo.class);
        when(nameInfo.isReferenced()).thenReturn(true);
        when(nameInfo.readsClosureVariables()).thenReturn(false);

        CrossModuleMethodMotion.Symbol symbol = mock(CrossModuleMethodMotion.Property.class);
        when(symbol instanceof CrossModuleMethodMotion.Property).thenReturn(true);
        when(nameInfo.getDeepestCommonModuleRef()).thenReturn(mock(JSModule.class));
        when(nameInfo.getDeclarations()).thenReturn(Collections.singletonList(symbol));

        Node valueParent = mock(Node.class);
        when(symbol.getValue()).thenReturn(mock(Node.class, RETURNS_DEEP_STUBS));
        when(symbol.getPrototype()).thenReturn(mock(Node.class));
        when(symbol.getValue().getParent()).thenReturn(valueParent);

        when(moduleGraph.dependsOn(any(JSModule.class), any(JSModule.class))).thenReturn(false);

        crossModuleMethodMotion.moveMethods(Collections.singletonList(nameInfo));

        verify(compiler, never()).reportCodeChange();
    }

    @Test
    public void testIdGenerator_hasGeneratedAnyIds_ReturnsTrueIfCurrentIdIsNotZero() {
        CrossModuleMethodMotion.IdGenerator generator = crossModuleMethodMotion.new IdGenerator();
        generator.currentId = 1;

        boolean result = generator.hasGeneratedAnyIds();

        assertEquals(true, result);
    }

    @Test
    public void testIdGenerator_hasGeneratedAnyIds_ReturnsFalseIfCurrentIdIsZero() {
        CrossModuleMethodMotion.IdGenerator generator = crossModuleMethodMotion.new IdGenerator();
        generator.currentId = 0;

        boolean result = generator.hasGeneratedAnyIds();

        assertEquals(false, result);
    }

    @Test
    public void testIdGenerator_newId_IncrementsCurrentIdBy1() {
        CrossModuleMethodMotion.IdGenerator generator = crossModuleMethodMotion.new IdGenerator();
        generator.currentId = 0;

        generator.newId();

        assertEquals(1, generator.currentId);
    }
}