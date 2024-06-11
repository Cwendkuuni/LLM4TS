import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import fi.vtt.noen.testgen.parser.*;
import java.io.*;

public class DaikonFormatterTest {

  private DaikonFormatter daikonFormatter;
  private String fileName = "testFile.txt";

  @Before
  public void setUp() throws IOException {
    daikonFormatter = new DaikonFormatter(fileName, true);
  }

  @Test
  public void testDaikonFormatter_constructor1() {
    assertNotNull(daikonFormatter);
  }

  @Test
  public void testDaikonFormatter_constructor2() throws IOException {
    List<String> inputs = new ArrayList<String>();
    inputs.add("input1");
    inputs.add("input2");
    daikonFormatter = new DaikonFormatter(fileName, inputs);
    assertNotNull(daikonFormatter);
  }

  @Test
  public void testClassForProperty_existingProperty() throws Exception {
    String property = "InputInterface1";
    Class clazz = DaikonFormatter.classForProperty(property);
    assertNotNull(clazz);
  }

  @Test
  public void testClassForProperty_nonExistingProperty() throws Exception {
    String property = "InputInterface3";
    Class clazz = DaikonFormatter.classForProperty(property);
    assertNull(clazz);
  }

  @Test
  public void testClassesForMultipleProperties_existingProperties() throws Exception {
    String prefix = "InputInterface";
    Collection<Class> classes = DaikonFormatter.classesForMultipleProperties(prefix);
    assertNotNull(classes);
    assertFalse(classes.isEmpty());
  }

  @Test
  public void testClassesForMultipleProperties_nonExistingProperties() throws Exception {
    String prefix = "InputInterface3";
    Collection<Class> classes = DaikonFormatter.classesForMultipleProperties(prefix);
    assertNotNull(classes);
    assertTrue(classes.isEmpty());
  }

  @Test
  public void testInputs() throws Exception {
    Collection<Class> inputs = DaikonFormatter.inputs();
    assertNotNull(inputs);
    assertFalse(inputs.isEmpty());
  }

  @Test
  public void testOutputs() throws Exception {
    Collection<Class> outputs = DaikonFormatter.outputs();
    assertNotNull(outputs);
    assertFalse(outputs.isEmpty());
  }

  @Test
  public void testHeader() {
    String header = daikonFormatter.header();
    assertNotNull(header);
    assertTrue(header.contains("decl-version 2.0"));
    assertTrue(header.contains("var-comparability implicit"));
  }

  @Test
  public void testFooter() {
    String footer = daikonFormatter.footer();
    assertNotNull(footer);
    assertTrue(footer.isEmpty());
  }

  @Test
  public void testFileNameExtension() {
    String extension = daikonFormatter.fileNameExtension();
    assertNotNull(extension);
    assertTrue(extension.equals("dtrace"));
  }

  @Test
  public void testObservations_simpleTrue() {
    ProgramRun run = new ProgramRun();
    String observations = daikonFormatter.observations(run);
    assertNotNull(observations);
    assertTrue(observations.isEmpty());
  }

  @Test
  public void testObservations_simpleFalse() {
    List<String> inputs = new ArrayList<String>();
    inputs.add("input1");
    inputs.add("input2");
    daikonFormatter = new DaikonFormatter(fileName, inputs);

    ProgramRun run = new ProgramRun();
    run.addEvent(new Event("input1", EventType.ENTER, Arrays.asList(
      new EventAttribute("attribute1", "type1", "value1"),
      new EventAttribute("attribute2", "type2", "value2")
    )));
    run.addEvent(new Event("input2", EventType.EXIT, Arrays.asList(
      new EventAttribute("attribute3", "type3", "value3"),
      new EventAttribute("attribute4", "type4", "value4")
    )));

    String observations = daikonFormatter.observations(run);
    assertNotNull(observations);
    assertFalse(observations.isEmpty());
    assertTrue(observations.contains("attribute1"));
    assertTrue(observations.contains("attribute2"));
    assertTrue(observations.contains("attribute3"));
    assertTrue(observations.contains("attribute4"));
    assertTrue(observations.contains("value1"));
    assertTrue(observations.contains("value2"));
    assertTrue(observations.contains("value3"));
    assertTrue(observations.contains("value4"));
  }

  @Test
  public void testSimplePoint() {
    Event event = new Event("input1", EventType.ENTER, Arrays.asList(
      new EventAttribute("attribute1", "type1", "value1"),
      new EventAttribute("attribute2", "type2", "value2")
    ));
    String comparability = "10";
    OutputBuffer out = new OutputBuffer();

    daikonFormatter.simplePoint(event, comparability, out);

    String output = out.toString();
    assertNotNull(output);
    assertTrue(output.contains("input1:::ENTER"));
    assertTrue(output.contains("variable attribute1"));
    assertTrue(output.contains("var-kind variable"));
    assertTrue(output.contains("dec-type type1"));
    assertTrue(output.contains("rep-type type1"));
    assertTrue(output.contains("comparability 10"));
    assertTrue(output.contains("value1"));
    assertTrue(output.contains("value2"));
  }

  @Test
  public void testPoint() {
    Event event = new Event("input1", EventType.ENTER, Arrays.asList(
      new EventAttribute("attribute1", "type1", "value1"),
      new EventAttribute("attribute2", "type2", "value2")
    ));
    String comparability = "11";
    OutputBuffer out = new OutputBuffer();

    daikonFormatter.point("input1", event, comparability, out);

    String output = out.toString();
    assertNotNull(output);
    assertTrue(output.contains("input1:::ENTER"));
    assertTrue(output.contains("variable attribute1"));
    assertTrue(output.contains("var-kind variable"));
    assertTrue(output.contains("dec-type type1"));
    assertTrue(output.contains("rep-type type1"));
    assertTrue(output.contains("comparability 11"));
    assertTrue(output.contains("value1"));
    assertTrue(output.contains("value2"));
  }

  @Test
  public void testDeclarations_newEvent() {
    Event event = new Event("input1", EventType.ENTER, Arrays.asList(
      new EventAttribute("attribute1", "type1", "value1"),
      new EventAttribute("attribute2", "type2", "value2")
    ));
    String eventName = "input1:::ENTER";
    String comparability = "22";
    OutputBuffer out = new OutputBuffer();

    daikonFormatter.declarations(event, eventName, out, comparability);

    String output = out.toString();
    assertNotNull(output);
    assertTrue(output.contains("ppt input1:::ENTER"));
    assertTrue(output.contains("ppt-type enter"));
    assertTrue(output.contains("variable attribute1"));
    assertTrue(output.contains("var-kind variable"));
    assertTrue(output.contains("dec-type type1"));
    assertTrue(output.contains("rep-type type1"));
    assertTrue(output.contains("comparability 22"));
  }

  @Test
  public void testAttributes() {
    Event event = new Event("input1", EventType.ENTER, Arrays.asList(
      new EventAttribute("attribute1", "type1", "value1"),
      new EventAttribute("attribute2", "type2", "value2")
    ));
    OutputBuffer out = new OutputBuffer();

    daikonFormatter.attributes(event, out);

    String output = out.toString();
    assertNotNull(output);
    assertTrue(output.contains("attribute1"));
    assertTrue(output.contains("attribute2"));
    assertTrue(output.contains("value1"));
    assertTrue(output.contains("value2"));
  }
}