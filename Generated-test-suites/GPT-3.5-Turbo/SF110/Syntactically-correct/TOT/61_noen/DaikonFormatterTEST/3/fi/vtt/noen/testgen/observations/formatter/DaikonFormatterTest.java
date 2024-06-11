import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatter;

    @Before
    public void setUp() {
        daikonFormatter = new DaikonFormatter("inputFile.txt", true);
    }

    @Test
    public void testConstructorWithSimpleOption() {
        assertTrue(daikonFormatter.simple);
        assertNull(daikonFormatter.inputs);
    }

    @Test
    public void testConstructorWithInputs() {
        daikonFormatter = new DaikonFormatter("inputFile.txt", Arrays.asList("input1", "input2"));
        assertFalse(daikonFormatter.simple);
        assertNotNull(daikonFormatter.inputs);
        assertEquals(2, daikonFormatter.inputs.size());
        assertTrue(daikonFormatter.inputs.contains("input1"));
        assertTrue(daikonFormatter.inputs.contains("input2"));
    }

    @Test
    public void testHeader() {
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, daikonFormatter.header());
    }

    @Test
    public void testFooter() {
        assertEquals("", daikonFormatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", daikonFormatter.fileNameExtension());
    }

    @Test
    public void testObservationsSimple() {
        ProgramRun programRun = createSimpleProgramRun();
        String expected = "";
        expected += "Calc:::ENTER\n";
        expected += "calculation,100,200,300,400,1\n";
        expected += "Calc:::EXIT1\n";
        expected += "Calc:::ENTER\n";
        expected += "input:::ENTER\n";
        expected += "input,1\n";
        expected += "input:::EXIT1\n";
        expected += "output:::ENTER\n";
        expected += "output,100\n";
        expected += "output:::EXIT1\n";
        expected += "calculation:::EXIT1\n";

        assertEquals(expected, daikonFormatter.observations(programRun));
    }

    @Test
    public void testObservationsWithInputs() {
        ProgramRun programRun = createProgramRunWithInputs();
        String expected = "";
        expected += "Calc:::ENTER\n";
        expected += "calculation,100,200,300,400,1\n";
        expected += "Calc:::EXIT1\n";
        expected += "Calc,Calc:::ENTER\n";
        expected += "calculation,100,200,300,400,1\n";
        expected += "Calc,Calc:::EXIT1\n";
        expected += "Calc,input:::ENTER\n";
        expected += "input,1\n";
        expected += "Calc,input:::EXIT1\n";
        expected += "Calc,output:::ENTER\n";
        expected += "output,100\n";
        expected += "Calc,output:::EXIT1\n";
        expected += "Calc,calculation:::EXIT1\n";

        assertEquals(expected, daikonFormatter.observations(programRun));
    }

    @Test
    public void testObservationsWithNestedMethodCalls() {
        ProgramRun programRun = createProgramRunWithNestedMethods();
        String expected = "";
        expected += "Calc:::ENTER\n";
        expected += "calculation,100,200,300,400,1\n";
        expected += "Calc:::EXIT1\n";
        expected += "Calc:::ENTER\n";
        expected += "calculation,100,200,300,400,1\n";
        expected += "Calc:::EXIT1\n";
        expected += "Calc,input:::ENTER\n";
        expected += "input,1\n";
        expected += "Calc,input:::EXIT1\n";
        expected += "Calc,output:::ENTER\n";
        expected += "output,100\n";
        expected += "Calc,output:::EXIT1\n";
        expected += "Calc,calculation:::EXIT1\n";
        expected += "Calc,Calc:::ENTER\n";
        expected += "calculation,100,200,300,400,1\n";
        expected += "Calc,Calc:::EXIT1\n";
        expected += "Calc,Calc:::ENTER\n";
        expected += "calculation,100,200,300,400,1\n";
        expected += "Calc,Calc:::EXIT1\n";
        expected += "Calc,Calc,input:::ENTER\n";
        expected += "input,1\n";
        expected += "Calc,Calc,input:::EXIT1\n";
        expected += "Calc,Calc,output:::ENTER\n";
        expected += "output,100\n";
        expected += "Calc,Calc,output:::EXIT1\n";
        expected += "Calc,Calc,calculation:::EXIT1\n";

        assertEquals(expected, daikonFormatter.observations(programRun));
    }

    private ProgramRun createSimpleProgramRun() {
        ProgramRun programRun = new ProgramRun();
        programRun.addEvent(new Event("Calc", false));
        programRun.addEventAttribute(new EventAttribute("calculation", "String", "100"));
        programRun.addEventAttribute(new EventAttribute("calculation", "String", "200"));
        programRun.addEventAttribute(new EventAttribute("calculation", "String", "300"));
        programRun.addEventAttribute(new EventAttribute("calculation", "String", "400"));
        programRun.addEventAttribute(new EventAttribute("calculation", "int", "1"));
        programRun.addEvent(new Event("Calc", true));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("input", true));
        programRun.addEventAttribute(new EventAttribute("input", "int", "1"));
        programRun.addEvent(new Event("input", false));
        programRun.addEvent(new Event("output", true));
        programRun.addEventAttribute(new EventAttribute("output", "int", "100"));
        programRun.addEvent(new Event("output", false));
        programRun.addEvent(new Event("calculation", true));

        return programRun;
    }

    private ProgramRun createProgramRunWithInputs() {
        ProgramRun programRun = createSimpleProgramRun();
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("Calc", true));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("input", true));
        programRun.addEvent(new Event("input", false));
        programRun.addEvent(new Event("output", true));
        programRun.addEvent(new Event("output", false));
        programRun.addEvent(new Event("calculation", true));

        return programRun;
    }

    private ProgramRun createProgramRunWithNestedMethods() {
        ProgramRun programRun = createProgramRunWithInputs();
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("Calc", true));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("input", true));
        programRun.addEvent(new Event("input", false));
        programRun.addEvent(new Event("output", true));
        programRun.addEvent(new Event("output", false));
        programRun.addEvent(new Event("calculation", true));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("Calc", true));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("Calc", false));
        programRun.addEvent(new Event("input", true));
        programRun.addEvent(new Event("input", false));
        programRun.addEvent(new Event("output", true));
        programRun.addEvent(new Event("output", false));
        programRun.addEvent(new Event("calculation", true));

        return programRun;
    }

}