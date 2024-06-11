import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
    
    @BeforeClass
    public static void setUpClass() {
        Variable.variableList = new ArrayList<>();
    }
    
    @AfterClass
    public static void tearDownClass() {
        Variable.variableList = null;
    }
    
    @Before
    public void setUp() {
        Variable.variableList.clear();
    }
    
    @After
    public void tearDown() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable() {
        // Typical use case
        assertTrue(Variable.addVariable("x"));
        
        // Edge case: Variable name with an asterisk
        assertTrue(Variable.addVariable("*y"));
        
        // Error scenario: Variable name already exists
        Variable.addVariable("z");
        assertFalse(Variable.addVariable("z"));
    }
    
    @Test
    public void testRemoveVariable() {
        // Typical use case
        Variable.addVariable("x");
        Variable.removeVariable("x");
        assertEquals(0, Variable.variableList.size());
        
        // Error scenario: Variable name does not exist
        Variable.removeVariable("y");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        // Typical use case
        Variable.addVariable("x");
        Variable.setVariableValue("x", 10);
        assertEquals(10, Variable.getVariableValue("x"));
        
        // Edge case: Variable name with an asterisk
        Variable.setVariableValue("x", 20);
        assertEquals(20, Variable.getVariableValue("*x"));
        
        // Error scenario: Variable name does not exist
        assertEquals(0, Variable.getVariableValue("y"));
    }
    
    @Test
    public void testSetVariableValue() {
        // Typical use case
        Variable.addVariable("x");
        Variable.setVariableValue("x", 10);
        assertEquals(10, Variable.getVariableValue("x"));
        
        // Error scenario: Variable name does not exist
        Variable.setVariableValue("y", 20);
        assertEquals(0, Variable.getVariableValue("y"));
    }
    
    @Test
    public void testFindVariable() {
        // Typical use case
        Variable.addVariable("x");
        Variable.setVariableValue("x", 10);
        assertEquals(0, Variable.findVariable("x"));
        
        // Edge case: Variable name with an ampersand
        Variable.setVariableValue("x", 20);
        assertEquals(20, Variable.findVariable("&x"));
        
        // Error scenario: Variable name does not exist
        assertEquals(-1, Variable.findVariable("y"));
    }
}