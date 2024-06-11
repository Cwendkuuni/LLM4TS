import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Node;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
    
    private AbstractCompiler compiler;
    private IdGenerator idGenerator;
    private AnalyzePrototypeProperties analyzer;
    private JSModuleGraph moduleGraph;
    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, false, false);
        moduleGraph = new JSModuleGraph();
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, false);
    }
    
    @Test
    public void testProcess() {
        Node externRoot = new Node();
        Node root = new Node();
        crossModuleMethodMotion.process(externRoot, root);
    }
    
    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion.NameInfo nameInfo = crossModuleMethodMotion.new NameInfo(new CrossModuleMethodMotion.Property(), false);
        crossModuleMethodMotion.moveMethods(Collections.singleton(nameInfo));
    }
    
    @Test
    public void testNewId() {
        int id = idGenerator.newId();
        assertEquals(0, id);
    }
}