import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain_Build() {
        String[] args = {"testPropertyFile", "build"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        LagoonCLI.main(args);
        System.out.flush();
        System.setOut(oldOut);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Building website"));
    }

    @Test
    public void testMain_Force() {
        String[] args = {"testPropertyFile", "force"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        LagoonCLI.main(args);
        System.out.flush();
        System.setOut(oldOut);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Force building website"));
    }

    @Test
    public void testMain_Interval() {
        String[] args = {"testPropertyFile", "10"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        LagoonCLI.main(args);
        System.out.flush();
        System.setOut(oldOut);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Building website"));
        assertTrue(output.contains("Ready"));
        assertTrue(output.contains("Thread still running"));
    }

    @Test
    public void testMain_NoArguments() {
        String[] args = {};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        LagoonCLI.main(args);
        System.out.flush();
        System.setOut(oldOut);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Syntax"));
    }

    @Test
    public void testMain_InvalidInterval() {
        String[] args = {"testPropertyFile", "interval"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        LagoonCLI.main(args);
        System.out.flush();
        System.setOut(oldOut);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Syntax"));
    }

    @Test
    public void testMain_InvalidPropertyFile() {
        String[] args = {"invalidPropertyFile"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldOut = System.out;
        System.setOut(ps);
        LagoonCLI.main(args);
        System.out.flush();
        System.setOut(oldOut);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("File not found"));
    }

    @Test
    public void testMain_ExceptionThrown() {
        String[] args = {"testPropertyFile", "exception"};
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream oldErr = System.err;
        System.setErr(ps);
        LagoonCLI.main(args);
        System.err.flush();
        System.setErr(oldErr);
        String output = new String(baos.toByteArray());
        assertTrue(output.contains("Error while initializing Lagoon"));
    }
}