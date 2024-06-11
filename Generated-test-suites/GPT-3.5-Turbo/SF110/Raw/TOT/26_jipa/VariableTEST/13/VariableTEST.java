import static org.junit.Assert.*;
import org.junit.*;

public class VariableTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // Initialize variableList with some initial values for testing
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 10);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Clean up variableList after testing
        Variable.removeVariable("var1");
        Variable.removeVariable("var2");
    }

    @Before
    public void setUp() throws Exception {
        // Reset varValue before each test case
        Variable.setVariableValue("var1", 0);
    }

    @After
    public void tearDown() throws Exception {
        // Clean up varValue after each test case
        Variable.setVariableValue("var1", 0);
    }

    @Test
    public void testAddVariable() {
        // Typical use case: Adding a new variable should return true
        assertTrue(Variable.addVariable("var3"));
        
        // Adding an existing variable should return false
        assertFalse(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Removing an existing variable should succeed
        Variable.removeVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1"));
        
        // Removing a non-existing variable should have no effect
        Variable.removeVariable("var3");
    }

    @Test
    public void testGetVariableValue() {
        // Getting value of an existing variable should return the correct value
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(10, Variable.getVariableValue("var2"));
        
        // Getting value of a non-existing variable should return 0
        assertEquals(0, Variable.getVariableValue("var3"));
        
        // Getting value of a reference variable should return the referenced value
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        // Setting value of an existing variable should update the value
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Setting value of a non-existing variable should have no effect
        Variable.setVariableValue("var3", 5);
        assertEquals(0, Variable.getVariableValue("var3"));
    }
}