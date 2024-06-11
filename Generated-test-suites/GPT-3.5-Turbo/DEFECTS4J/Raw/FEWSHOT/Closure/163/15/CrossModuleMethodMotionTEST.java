package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;


public class CrossModuleMethodMotionTest {
    
    private AbstractCompiler compiler;
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private boolean canModifyExterns;
    private CrossModuleMethodMotion.CrossModuleMethodMotion actualCrossModuleMethodMotion;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        canModifyExterns = false;
        actualCrossModuleMethodMotion = new CrossModuleMethodMotion.CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    }

    @Test
    public void testProcess() {
        Node externRoot = mock(Node.class);
        Node root = mock(Node.class);

        actualCrossModuleMethodMotion.process(externRoot, root);
    }

    @Test
    public void testMoveMethods() {
        Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<AnalyzePrototypeProperties.NameInfo>();
        AnalyzePrototypeProperties.NameInfo nameInfo = mock(AnalyzePrototypeProperties.NameInfo.class);
        allNameInfo.add(nameInfo);

        actualCrossModuleMethodMotion.moveMethods(allNameInfo);
    }

    @Test
    public void testHasGeneratedAnyIds() {
        boolean result = idGenerator.hasGeneratedAnyIds();

        assertEquals(false, result);
    }

    @Test
    public void testNewId() {
        int result = idGenerator.newId();

        assertEquals(0, result);
    }
}