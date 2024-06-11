package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
    private AbstractCompiler compiler;
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private boolean canModifyExterns;
    private JSModuleGraph moduleGraph;

    @Before
    public void setUp() {
        compiler = new Compiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        canModifyExterns = true;
        moduleGraph = new JSModuleGraph();
    }

    @Test
    public void testProcess() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        crossModuleMethodMotion.process(externRoot, root);

        // Assert some conditions after process() is called
        // ...
    }

    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Collection<NameInfo> allNameInfo = new ArrayList<>();
        crossModuleMethodMotion.moveMethods(allNameInfo);

        // Assert some conditions after moveMethods() is called
        // ...
    }

    @Test
    public void testHasGeneratedAnyIds() {
        idGenerator.hasGeneratedAnyIds();
        
        // Assert some conditions after hasGeneratedAnyIds() is called
        // ...
    }

    @Test
    public void testNewId() {
        idGenerator.newId();
        
        // Assert some conditions after newId() is called
        // ...
    }
}