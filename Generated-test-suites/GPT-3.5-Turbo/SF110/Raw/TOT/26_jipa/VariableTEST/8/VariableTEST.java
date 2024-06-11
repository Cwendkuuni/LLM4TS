import static org.junit.Assert.*;
import org.junit.*;

public class VariableTest {

    @BeforeClass
    public static void setUpClass() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @AfterClass
    public static void tearDownClass() {
        Variable.variableList.clear();
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
    public void testAddVariable_typicalCase() {
        assertTrue(Variable.addVariable("varA"));
    }

    @Test
    public void testAddVariable_existingVariable() {
        Variable.addVariable("varA");
        assertFalse(Variable.addVariable("varA"));
    }

    @Test
    public void testRemoveVariable_typicalCase() {
        Variable.addVariable("varA");
        Variable.addVariable("varB");
        Variable.removeVariable("varA");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_nonExistingVariable() {
        Variable.addVariable("varA");
        Variable.addVariable("varB");
        Variable.removeVariable("varC");
        assertEquals(2, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue_typicalCase() {
        Variable.addVariable("varA");
        Variable.setVariableValue("varA", 5);
        assertEquals(5, Variable.getVariableValue("varA"));
    }

    @Test
    public void testGetVariableValue_nonExistingVariable() {
        assertEquals(0, Variable.getVariableValue("varA"));
    }

    @Test
    public void testGetVariableValue_referencedVariable() {
        Variable.addVariable("varA");
        Variable.setVariableValue("varA", 5);
        assertEquals(5, Variable.getVariableValue("*varA"));
    }

    @Test
    public void testSetVariableValue_typicalCase() {
        Variable.addVariable("varA");
        Variable.setVariableValue("varA", 5);
        assertEquals(5, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("varA", 5);
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testSetVariableValue_existingVariable() {
        Variable.addVariable("varA");
        Variable.setVariableValue("varA", 5);
        Variable.setVariableValue("varA", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testFindVariable_typicalCase() {
        Variable.addVariable("varA");
        Variable.addVariable("varB");
        assertEquals(1, Variable.findVariable("varB"));
    }

    @Test
    public void testFindVariable_nonExistingVariable() {
        assertEquals(-1, Variable.findVariable("varA"));
    }

    @Test
    public void testFindVariable_referencedVariable() {
        Variable.addVariable("varA");
        Variable.setVariableValue("varA", 5);
        assertEquals(5, Variable.findVariable("&*varA"));
    }

    @Test
    public void testFindVariable_referencedVariableNoReferenceFound() {
        Variable.addVariable("varA");
        Variable.setVariableValue("varA", 5);
        assertEquals(-1, Variable.findVariable("*varA"));
    }

}