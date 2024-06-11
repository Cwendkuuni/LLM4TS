package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        String actual = formatter.header();
        assertEquals(expected, actual);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "";
        String actual = formatter.footer();
        assertEquals(expected, actual);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        String expected = "dtrace";
        String actual = formatter.fileNameExtension();
        assertEquals(expected, actual);
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("fileName", true);
        ProgramRun run = new ProgramRun();
        String expected = ""; // Set the expected value for the observations method
        String actual = formatter.observations(run);
        assertEquals(expected, actual);
    }
}