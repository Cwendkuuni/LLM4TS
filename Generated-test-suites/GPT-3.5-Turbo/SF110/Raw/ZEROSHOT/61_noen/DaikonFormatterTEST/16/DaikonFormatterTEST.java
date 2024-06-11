package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import fi.vtt.noen.testgen.model.*;

import java.util.Collection;
import java.util.HashSet;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatter;

    @Before
    public void setUp() {
        daikonFormatter = new DaikonFormatter("testFile.txt", true);
    }

    @Test
    public void testConstructor() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.txt", false);
            Assert.assertNotNull(formatter);
        } catch (Exception e) {
            Assert.fail("Failed to create DaikonFormatter object");
        }
    }

    @Test
    public void testHeader() {
        String header = daikonFormatter.header();
        Assert.assertNotNull(header);
        Assert.assertTrue(header.contains("decl-version 2.0"));
        Assert.assertTrue(header.contains("var-comparability implicit"));
    }

    @Test
    public void testFooter() {
        String footer = daikonFormatter.footer();
        Assert.assertNotNull(footer);
        Assert.assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() {
        String extension = daikonFormatter.fileNameExtension();
        Assert.assertNotNull(extension);
        Assert.assertEquals("dtrace", extension);
    }

    @Test
    public void testObservations() {
        ProgramRun run = new ProgramRun();
        try {
            String observations = daikonFormatter.observations(run);
            Assert.assertNotNull(observations);
        } catch (Exception e) {
            Assert.fail("Failed to get observations");
        }
    }

    @Test
    public void testSimplePoint() {
        Event event = new Event("event1", false);
        String comparability = "10";
        OutputBuffer buffer = new OutputBuffer();
        daikonFormatter.simplePoint(event, comparability, buffer);

        String expectedPoint = "event1:::ENTER\n\n" +
                "  variable event1\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.Object\n" +
                "    rep-type java.lang.Object\n" +
                "    comparability 10\n";
        Assert.assertEquals(expectedPoint, buffer.toString());
    }

    @Test
    public void testPoint() {
        Event previousEvent = new Event("event1", false);
        Event event = new Event("event2", false);
        String comparability = "11";
        OutputBuffer buffer = new OutputBuffer();
        daikonFormatter.point("event1,event2", previousEvent, comparability, buffer);

        String expectedPoint = "event1,event2:::ENTER\n\n" +
                "  variable event1,event2\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.Object\n" +
                "    rep-type java.lang.Object\n" +
                "    comparability 11\n";
        Assert.assertEquals(expectedPoint, buffer.toString());
    }

    @Test
    public void testDeclarations() {
        Event event = new Event("event1", false);
        String eventName = "event1:::ENTER";
        OutputBuffer buffer = new OutputBuffer();
        String comparability = "22";

        daikonFormatter.declarations(event, eventName, buffer, comparability);

        String expectedDeclarations = "\nppt event1:::ENTER\n" +
                "ppt-type enter\n" +
                "  variable event1\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.Object\n" +
                "    rep-type java.lang.Object\n" +
                "    comparability 22\n";
        Assert.assertEquals(expectedDeclarations, buffer.toString());
    }

    @Test
    public void testAttributes() {
        Event event = new Event("event1", false);
        OutputBuffer buffer = new OutputBuffer();

        ArrayAttribute arrayAttribute = new ArrayAttribute("attribute1", "java.lang.String", new HashSet<>());
        event.addAttribute(arrayAttribute);

        Attribute attribute = new Attribute("attribute2", "java.lang.Integer", "1");
        event.addAttribute(attribute);

        daikonFormatter.attributes(event, buffer);

        String expectedAttributes = "attribute1[]\n" +
                "attribute2\n" +
                "1";
        Assert.assertEquals(expectedAttributes, buffer.toString());
    }
}