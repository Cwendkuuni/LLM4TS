import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Variable.variableList = new ArrayList<Variable>();
    }
    
    @Before
    public void setUp() throws Exception {
        
    }

    @Test
    public void testGetValue() {
        Variable variable = new Variable("testVariable");
        int expectedValue = 0;
        int actualValue = variable.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testSetValue() {
        Variable variable = new Variable("testVariable");
        int expectedValue = 10;
        variable.setValue(expectedValue);
        int actualValue = variable.getValue();
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetName() {
        Variable variable = new Variable("testVariable");
        String expectedName = "testVariable";
        String actualName = variable.getName();
        assertEquals(expectedName, actualName);
    }
    
    @Test
    public void testAddVariable() {
        String varName = "testVariable";
        boolean expectedValue = true;
        boolean actualValue = Variable.addVariable(varName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testRemoveVariable() {
        String varName = "testVariable";
        Variable.addVariable(varName);
        Variable.removeVariable(varName);
        int expectedValue = -1;
        int actualValue = Variable.findVariable(varName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetVariableValue() {
        String varName = "testVariable";
        int expectedValue = 0;
        int actualValue = Variable.getVariableValue(varName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testSetVariableValue() {
        String varName = "testVariable";
        int varValue = 10;
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, varValue);
        int expectedValue = varValue;
        int actualValue = Variable.getVariableValue(varName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testFindVariable() {
        String varName = "testVariable";
        Variable.addVariable(varName);
        int expectedValue = 0;
        int actualValue = Variable.findVariable(varName);
        assertEquals(expectedValue, actualValue);
    }

    @After
    public void tearDown() throws Exception {
        
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Variable.variableList = null;
    }

}