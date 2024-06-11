import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DaikonFormatterTest {

  @Test
  public void testConstructorSimple() {
    try {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", true);
        assertNotNull(daikonFormatter);
    } catch (IOException e) {
        fail("Exception caught");
    }
  }
  
  @Test
  public void testConstructorWithInputs() {
    try {
        List<String> inputs = new ArrayList<String>();
        inputs.add("input1");
        inputs.add("input2");
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", inputs);
        assertNotNull(daikonFormatter);
    } catch (IOException e) {
        fail("Exception caught");
    }
  }
  
  @Test
  public void testHeader() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", true);
    String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
    assertEquals(expectedHeader, daikonFormatter.header());
  }
  
  @Test
  public void testFooter() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", true);
    assertEquals("", daikonFormatter.footer());
  }
  
  @Test
  public void testFileNameExtension() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", true);
    assertEquals("dtrace", daikonFormatter.fileNameExtension());
  }
  
  @Test
  public void testObservationsSimple() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", true);
    ProgramRun programRun = new ProgramRun();
    programRun.addEvent(new Event("Event1", true, new EventAttribute("Attribute1", "Type1", "Value1")));
    programRun.addEvent(new Event("Event2", false, new EventAttribute("Attribute2", "Type2", "Value2")));
    String expectedObservations = "\"Event1:::ENTER\"\n" +
                                  "  variable Attribute1\n" +
                                  "    var-kind variable\n" +
                                  "    dec-type Type1\n" +
                                  "    rep-type Type1\n" +
                                  "    comparability 10\n" +
                                  "\n" +
                                  "\"Event2:::ENTER\"\n" +
                                  "  variable Attribute2\n" +
                                  "    var-kind variable\n" +
                                  "    dec-type Type2\n" +
                                  "    rep-type Type2\n" +
                                  "    comparability 11\n" +
                                  "\n" +
                                  "\"Event2:::EXIT1\"\n" +
                                  "  variable Attribute2\n" +
                                  "    var-kind variable\n" +
                                  "    dec-type Type2\n" +
                                  "    rep-type Type2\n" +
                                  "    comparability 22\n" +
                                  "\n";
    assertEquals(expectedObservations, daikonFormatter.observations(programRun));
  }
  
  @Test
  public void testObservationsComplex() {
    List<String> inputs = new ArrayList<String>();
    inputs.add("input1");
    inputs.add("input2");
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", inputs);
    ProgramRun programRun = new ProgramRun();
    programRun.addEvent(new Event("Event1", true, new EventAttribute("Attribute1", "Type1", "Value1")));
    programRun.addEvent(new Event("Event2", false, new EventAttribute("Attribute2", "Type2", "Value2")));
    String expectedObservations = "\"Event1,Event2:::ENTER\"\n" +
                                  "  variable Attribute1\n" +
                                  "    var-kind variable\n" +
                                  "    dec-type Type1\n" +
                                  "    rep-type Type1\n" +
                                  "    comparability 22\n" +
                                  "\n";
    assertEquals(expectedObservations, daikonFormatter.observations(programRun));
  }
  
  @Test
  public void testSimplePoint() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", true);
    Event event = new Event("Event1", false, new EventAttribute("Attribute1", "Type1", "Value1"));
    OutputBuffer out = new OutputBuffer();
    String expectedOutput = "\"Event1:::ESHG\"\n" +
                            "  variable Attribute1\n" +
                            "    var-kind variable\n" +
                            "    dec-type Type1\n" +
                            "    rep-type Type1\n" +
                            "    comparability 10\n" +
                            "\n";
    daikonFormatter.simplePoint(event, "10", out);
    assertEquals(expectedOutput, out.toString());
  }
  
  @Test
  public void testPoint() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", false);
    Event event = new Event("Event1", false, new EventAttribute("Attribute1", "Type1", "Value1"));
    OutputBuffer out = new OutputBuffer();
    String expectedOutput = "\"Event1:::ENTER\"\n" +
                            "  variable Attribute1\n" +
                            "    var-kind variable\n" +
                            "    dec-type Type1\n" +
                            "    rep-type Type1\n" +
                            "    comparability 11\n" +
                            "\n";
    daikonFormatter.point("Event1", event, "11", out);
    assertEquals(expectedOutput, out.toString());
  }
  
  @Test
  public void testDeclarations() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", false);
    Event event = new Event("Event1", false, new EventAttribute("Attribute1", "Type1", "Value1"));
    OutputBuffer out = new OutputBuffer();
    String expectedOutput = "\n" +
                            "ppt Event1:::ENTER\n" +
                            "ppt-type enter\n" +
                            "  variable Attribute1\n" +
                            "    var-kind variable\n" +
                            "    dec-type Type1\n" +
                            "    rep-type Type1\n" +
                            "    comparability 11\n";
    daikonFormatter.declarations(event, "Event1:::ENTER", out, "11");
    assertEquals(expectedOutput, out.toString());
  }
  
  @Test
  public void testAttributes() {
    DaikonFormatter daikonFormatter = new DaikonFormatter("test.dtrace", false);
    Event event = new Event("Event1", false, new EventAttribute("Attribute1", "Type1", "Value1"));
    OutputBuffer out = new OutputBuffer();
    String expectedOutput = "Attribute1\nValue11\n";
    daikonFormatter.attributes(event, out);
    assertEquals(expectedOutput, out.toString());
  }
}