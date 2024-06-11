import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {
    
    private AbstractCompiler compiler;
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
    }
    
    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(compiler, idGenerator, false);
        
        // Create sample NameInfo, Symbol and Property
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        CrossModuleMethodMotion.Property prop = new CrossModuleMethodMotion.Property();
        
        // Create sample nodes for the Property
        prop.setValue(new Node(Token.FUNCTION));
        prop.setPrototype(new Node(Token.OBJECTLIT));
        
        // Add the Property to the Symbol
        symbol.add(prop);
        
        // Set the deepest common module reference to null to test error handling
        nameInfo.setDeepestCommonModuleRef(null);
        
        // Add the Symbol to the NameInfo
        nameInfo.add(symbol);
        
        // Call the moveMethods method
        methodMotion.moveMethods(Collections.singletonList(nameInfo));
        
        // Verify that the appropriate error is reported
        Assert.assertEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND", compiler.getErrors().get(0).getType().key);
    }
    
    @Test
    public void testIdGenerator() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        
        // Verify that the initial id is 0
        Assert.assertEquals(0, idGenerator.newId());
        
        // Verify that the generated id is incremented by 1 each time
        Assert.assertEquals(1, idGenerator.newId());
        Assert.assertEquals(2, idGenerator.newId());
    }
    
}