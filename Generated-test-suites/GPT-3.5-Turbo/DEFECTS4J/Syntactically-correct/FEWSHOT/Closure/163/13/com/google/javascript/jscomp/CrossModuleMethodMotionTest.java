
package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {
    
    private AbstractCompiler compiler;
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private JSModuleGraph moduleGraph;
    private Node externRoot;
    private Node root;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        moduleGraph = new JSModuleGraph();
        externRoot = new Node();
        root = new Node();
    }
    
    @Test
    public void testProcess() {
        CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
        
        // Case 1: moduleGraph is null
        pass.process(externRoot, root);
        // no methods will be moved, since there's only 1 module
        
        // Case 2: moduleGraph has fewer than 2 modules
        moduleGraph.addModule(new JSModule(""));
        pass.process(externRoot, root);
        // no methods will be moved, since there's only 1 module
        
        // Case 3: moduleGraph has 2 or more modules
        moduleGraph.addModule(new JSModule(""));
        pass.process(externRoot, root);
        // methods will be moved deeper in the module graph
        
        // Additional test cases can be added to further test the process() method
        
        // assertEquals(expected, actual);
    }
    
    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, false);
        
        // Case 1: allNameInfo contains unreferenced name info
        Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<>();
        allNameInfo.add(new AnalyzePrototypeProperties.NameInfo());
        
        pass.moveMethods(allNameInfo);
        // the code below can't do anything with the unreferenced name info, so it should be skipped
        
        // Case 2: allNameInfo contains name info without declarations
        allNameInfo = new ArrayList<>();
        AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
        nameInfo.setReferenced(true);
        allNameInfo.add(nameInfo);
        
        pass.moveMethods(allNameInfo);
        // the code below can't do anything with name info without declarations, so it should be skipped
        
        // Additional test cases can be added to further test the moveMethods() method
        
        // assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructor() {
        Compiler compiler = new Compiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = false;
        
        CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        
        assertNull(pass.compiler);
        assertEquals(idGenerator, pass.idGenerator);
        assertNull(pass.moduleGraph);
        assertNotNull(pass.analyzer);
    }
    
    @Test
    public void testNewId() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        
        assertEquals(0, idGenerator.newId());
        assertEquals(1, idGenerator.newId());
        assertEquals(2, idGenerator.newId());
        // ...
        
        // Additional test cases can be added to further test the newId() method
        
        // assertEquals(expected, actual);
    }
}
