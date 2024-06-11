import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.CrossModuleMethodMotion.IdGenerator;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;

public class CrossModuleMethodMotionTest {
    
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        IdGenerator idGenerator = new IdGenerator();
        boolean canModifyExterns = true;
        CrossModuleMethodMotion cmm = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        
        Node externRoot = new Node(NodeToken.OBJECT);
        Node root = new Node(NodeToken.OBJECT);
        
        cmm.process(externRoot, root);
        
        // Implement test case here
    }
    
    @Test
    public void testMoveMethods() {
        AbstractCompiler compiler = new AbstractCompiler();
        IdGenerator idGenerator = new IdGenerator();
        boolean canModifyExterns = true;
        CrossModuleMethodMotion cmm = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        
        Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<AnalyzePrototypeProperties.NameInfo>();
        
        cmm.moveMethods(allNameInfo);
        
        // Implement test case here
    }
    
    @Test
    public void testHasGeneratedAnyIds() {
        IdGenerator idGenerator = new IdGenerator();
        
        boolean actual = idGenerator.hasGeneratedAnyIds();
        
        assertFalse(actual);
    }
    
    @Test
    public void testNewId() {
        IdGenerator idGenerator = new IdGenerator();
        
        int actual = idGenerator.newId();
        
        // Implement test case here
    }
}