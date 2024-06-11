package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    @Test
    public void testConstructor() {
        AbstractCompiler compiler = new Compiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion motion = new CrossModuleMethodMotion(compiler, idGenerator, true);

        assertNotNull(motion);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new Compiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion motion = new CrossModuleMethodMotion(compiler, idGenerator, true);

        Node externRoot = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        motion.process(externRoot, root);

        // Test that process completes without errors
    }

    @Test
    public void testMoveMethods() {
        AbstractCompiler compiler = new Compiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion motion = new CrossModuleMethodMotion(compiler, idGenerator, true);

        Collection<AnalyzePrototypeProperties.NameInfo> nameInfoList = new ArrayList<>();
        // Add some dummy NameInfo objects for testing

        motion.moveMethods(nameInfoList);

        // Test that moveMethods executes without errors
    }

    @Test
    public void testIdGenerator() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();

        assertFalse(idGenerator.hasGeneratedAnyIds());

        int id = idGenerator.newId();

        assertTrue(idGenerator.hasGeneratedAnyIds());
        assertEquals(1, id);
    }
}