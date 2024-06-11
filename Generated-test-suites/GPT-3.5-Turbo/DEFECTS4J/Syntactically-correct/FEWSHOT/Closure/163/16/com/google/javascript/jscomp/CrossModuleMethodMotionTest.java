import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

    private AbstractCompiler compiler;
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private boolean canModifyExterns;
    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        canModifyExterns = true;
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    }

    @Test
    public void testProcessWithNoModules() {
        Node externRoot = IR.root();
        Node root = IR.root();

        crossModuleMethodMotion.process(externRoot, root);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcessWithOneModule() {
        JSModule module = new JSModule("testModule");
        compiler.setModuleGraph(new JSModule[] { module });

        Node externRoot = IR.root();
        Node root = IR.root().addChildToFront(IR.empty());

        crossModuleMethodMotion.process(externRoot, root);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcessWithMultipleModules() {
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        compiler.setModuleGraph(new JSModule[] { module1, module2 });

        Node externRoot = IR.root();
        Node root = IR.root().addChildToFront(IR.empty());

        crossModuleMethodMotion.process(externRoot, root);

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcessWithMoveMethods() {
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        compiler.setModuleGraph(new JSModule[] { module1, module2 });

        Node externRoot = IR.root();
        Node root = IR.root().addChildToFront(IR.empty());

        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypePropertiesMock = Mockito.mock(CrossModuleMethodMotion.AnalyzePrototypeProperties.class);
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(IR.function(IR.name(""), IR.paramList(), IR.block()));
        property.setModule(module1);
        property.setPrototype(IR.name("prototype"));
        property.setDefinitions(Lists.newArrayList());
        property.setDeepestCommonModuleRef(module2);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo("name", analyzePrototypePropertiesMock);
        nameInfo.addDeclaration(property);
        Mockito.when(analyzePrototypePropertiesMock.getAllNameInfo()).thenReturn(Lists.newArrayList(nameInfo));
        crossModuleMethodMotion = Mockito.spy(crossModuleMethodMotion);
        Mockito.doNothing().when(crossModuleMethodMotion).moveMethods(Mockito.any(Collection.class));

        crossModuleMethodMotion.process(externRoot, root);

        Mockito.verify(crossModuleMethodMotion, Mockito.times(1)).moveMethods(Mockito.any(Collection.class));
    }

    @Test
    public void testMoveMethods() {
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        compiler.setModuleGraph(new JSModule[] { module1, module2 });

        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypePropertiesMock = Mockito.mock(CrossModuleMethodMotion.AnalyzePrototypeProperties.class);
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(IR.function(IR.name(""), IR.paramList(), IR.block()));
        property.setModule(module1);
        property.setPrototype(IR.name("prototype"));
        property.setDefinitions(Lists.newArrayList());
        property.setDeepestCommonModuleRef(module2);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo("name", analyzePrototypePropertiesMock);
        nameInfo.addDeclaration(property);

        crossModuleMethodMotion.moveMethods(Lists.newArrayList(nameInfo));

        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethodsWithUnreferencedNameInfo() {
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        compiler.setModuleGraph(new JSModule[] { module1, module2 });

        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypePropertiesMock = Mockito.mock(CrossModuleMethodMotion.AnalyzePrototypeProperties.class);
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        property.setValue(IR.function(IR.name(""), IR.paramList(), IR.block()));
        property.setModule(module1);
        property.setPrototype(IR.name("prototype"));
        property.setDefinitions(Lists.newArrayList());
        property.setDeepestCommonModuleRef(null);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo("name", analyzePrototypePropertiesMock);
        nameInfo.setReferenced(false);
        nameInfo.addDeclaration(property);

        crossModuleMethodMotion.moveMethods(Lists.newArrayList(nameInfo));

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethodsSkippingGetterDef() {
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        compiler.setModuleGraph(new JSModule[] { module1, module2 });

        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypePropertiesMock = Mockito.mock(CrossModuleMethodMotion.AnalyzePrototypeProperties.class);
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        Node value = IR.function(IR.name(""), IR.paramList(), IR.block());
        Node valueParent = IR.getterDef(IR.name("name"), value);
        value.setParent(valueParent);
        property.setValue(valueParent);
        property.setModule(module1);
        property.setPrototype(IR.name("prototype"));
        property.setDefinitions(Lists.newArrayList());
        property.setDeepestCommonModuleRef(module2);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo("name", analyzePrototypePropertiesMock);
        nameInfo.addDeclaration(property);

        crossModuleMethodMotion.moveMethods(Lists.newArrayList(nameInfo));

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethodsSkippingSetterDef() {
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        compiler.setModuleGraph(new JSModule[] { module1, module2 });

        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypePropertiesMock = Mockito.mock(CrossModuleMethodMotion.AnalyzePrototypeProperties.class);
        CrossModuleMethodMotion.Property property = new CrossModuleMethodMotion.Property();
        Node value = IR.function(IR.name(""), IR.paramList(), IR.block());
        Node valueParent = IR.setterDef(IR.name("name"), value);
        value.setParent(valueParent);
        property.setValue(valueParent);
        property.setModule(module1);
        property.setPrototype(IR.name("prototype"));
        property.setDefinitions(Lists.newArrayList());
        property.setDeepestCommonModuleRef(module2);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo("name", analyzePrototypePropertiesMock);
        nameInfo.addDeclaration(property);

        crossModuleMethodMotion.moveMethods(Lists.newArrayList(nameInfo));

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethodsNullDeepestCommonModuleRef() {
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        compiler.setModuleGraph(new JSModule[] { module1, module2 });

        crossModuleMethodMotion.moveMethods(Lists.newArrayList());

        Mockito.verify(compiler, Mockito.times(1)).report(Mockito.any(JSError.class));
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethodsEmptyDeclarations() {
        crossModuleMethodMotion.moveMethods(Lists.newArrayList());

        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testMoveMethodsEmptyDeclarationsButGeneratedIds() {
        idGenerator.newId();
        idGenerator.newId();

        crossModuleMethodMotion.moveMethods(Lists.newArrayList());

        assertTrue(idGenerator.hasGeneratedAnyIds());
    }
}