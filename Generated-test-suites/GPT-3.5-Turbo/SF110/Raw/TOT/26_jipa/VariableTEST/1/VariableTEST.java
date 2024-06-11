import static org.junit.Assert.*;
import org.junit.*;

public class VariableTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Variable.variableList.clear();
    }

    @Before
    public void setUp() throws Exception {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        Variable.setVariableValue("var3", 10);
    }

    @After
    public void tearDown() throws Exception {
        Variable.removeVariable("var1");
        Variable.removeVariable("var2");
        Variable.removeVariable("var3");
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var4"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.removeVariable("var1");
        assertEquals(2, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(10, Variable.getVariableValue("*var3"));
        assertEquals(0, Variable.getVariableValue("var5"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.setVariableValue("var1", 7);
        assertEquals(7, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var2", -2);
        assertEquals(-2, Variable.getVariableValue("var2"));
        Variable.setVariableValue("var4", 3);
        assertEquals(0, Variable.getVariableValue("var4"));
    }

    @Test
    public void testFindVariable() {
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertEquals(2, Variable.findVariable("var3"));
        assertEquals(-1, Variable.findVariable("var4"));
    }

    @Test
    public void testAddVariableEdgeCase() {
        Variable.addVariable("variableNameWithSpecialCharacters@#$%");
        assertEquals(4, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariableEdgeCase() {
        Variable.removeVariable("var5");
        assertEquals(3, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValueEdgeCase() {
        assertEquals(0, Variable.getVariableValue(""));
        assertEquals(0, Variable.getVariableValue("&var1"));
    }

    @Test
    public void testSetVariableValueEdgeCase() {
        Variable.setVariableValue("var1", 1000000);
        assertEquals(1000000, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var2", -1000000);
        assertEquals(-1000000, Variable.getVariableValue("var2"));
    }

    @Test
    public void testFindVariableEdgeCase() {
        Variable.addVariable("var1");
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1"));
    }
}