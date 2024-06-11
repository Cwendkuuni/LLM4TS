import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {
  
    @BeforeClass
    public static void setUpClass() {
        Variable.variableList = new ArrayList<Variable>();
    }
  
    @Before
    public void setUp() {
        Variable.variableList.clear();
        Variable.addVariable("var1");
        Variable.addVariable("var2");
    }

    @After
    public void tearDown() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        assertEquals(true, Variable.addVariable("var3"));
        assertEquals(false, Variable.addVariable("var1"));
    }
  
    @Test
    public void testRemoveVariable() {
        Variable.removeVariable("var1");
        assertEquals(-1, Variable.findVariable("var1"));
    }
  
    @Test
    public void testGetVariableValue() {
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var1", 20);
        Variable.setVariableValue("var2", 30);
        assertEquals(20, Variable.getVariableValue("var1"));
        assertEquals(30, Variable.getVariableValue("var2"));
    }
  
    @Test
    public void testSetVariableValue() {
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var1", 20);
        Variable.setVariableValue("var2", 30);
        assertEquals(20, Variable.getVariableValue("var1"));
        assertEquals(30, Variable.getVariableValue("var2"));

        Variable.setVariableValue("var3", 40);
        assertEquals(0, Variable.getVariableValue("var3"));
    }
  
    @Test
    public void testFindVariable() {
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertEquals(-1, Variable.findVariable("var3"));
    }
  
    @AfterClass
    public static void tearDownClass() {
        Variable.variableList = null;
    }

}