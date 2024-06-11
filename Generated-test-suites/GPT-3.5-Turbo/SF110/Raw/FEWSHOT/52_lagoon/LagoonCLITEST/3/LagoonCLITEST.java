
package nu.staldal.lagoon;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LagoonCLITest {

    @Test
    public void testMain_withEmptyArgs() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testMain_withPropertyFile_build() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"property_file_path", "build"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Building website...\n...with errors...\n", outContent.toString());
    }

    @Test
    public void testMain_withPropertyFile_force() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"property_file_path", "force"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Force building website...\n...with errors...\n", outContent.toString());
    }

    @Test
    public void testMain_withPropertyFile_interval() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"property_file_path", "100"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Building website...\n...with errors...\nin 0 s\n", outContent.toString());
    }

    @Test
    public void testMain_withPropertyFile_invalidInterval() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"property_file_path", "invalid"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testMain_withSitemapFile_build() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"sitemap_file_path", "build"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Building website...\n...with errors...\n", outContent.toString());
    }

    @Test
    public void testMain_withSitemapFile_force() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"sitemap_file_path", "force"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Force building website...\n...with errors...\n", outContent.toString());
    }

    @Test
    public void testMain_withSitemapFile_interval() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"sitemap_file_path", "100"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Building website...\n...with errors...\nin 0 s\n", outContent.toString());
    }

    @Test
    public void testMain_withSitemapFile_invalidInterval() {
        // Set up
        System.setOut(new PrintStream(outContent));
        String[] args = {"sitemap_file_path", "invalid"};

        // Test
        LagoonCLI.main(args);

        // Assertions
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testMain_withException_AuthenticationMissingException() throws FileNotFoundException, AuthenticationMissingException {
        // Set up
        System.setErr(new PrintStream(errContent));
        String[] args = {"property_file_path"};

        LagoonCLI lagoonCLI = new LagoonCLI();

        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        when(fileInputStream.read()).thenThrow(new AuthenticationMissingException("Password is required but not specified"));

        // Test
        lagoonCLI.main(args);

        // Assertions
        assertEquals("Error while initializing Lagoon:\nPassword is required but not specified\n", errContent.toString());
    }

    @Test
    public void testMain_withException_AuthenticationException() throws FileNotFoundException, IOException, AuthenticationException {
        // Set up
        System.setErr(new PrintStream(errContent));
        String[] args = {"property_file_path"};

        LagoonCLI lagoonCLI = new LagoonCLI();

        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        when(fileInputStream.read()).thenThrow(new AuthenticationException("Incorrect password"));

        // Test
        lagoonCLI.main(args);

        // Assertions
        assertEquals("Error while initializing Lagoon:\nIncorrect password\n", errContent.toString());
    }

    @Test
    public void testMain_withException_FileNotFoundException() throws FileNotFoundException {
        // Set up
        System.setErr(new PrintStream(errContent));
        String[] args = {"property_file_path"};

        LagoonCLI lagoonCLI = new LagoonCLI();

        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        when(fileInputStream.read()).thenThrow(new FileNotFoundException("File not found"));

        // Test
        lagoonCLI.main(args);

        // Assertions
        assertEquals("Error while initializing Lagoon:\nFile not found: File not found\n", errContent.toString());
    }

    @Test
    public void testMain_withIOException() throws IOException {
        // Set up
        System.setErr(new PrintStream(errContent));
        String[] args = {"property_file_path"};

        LagoonCLI lagoonCLI = new LagoonCLI();

        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        when(fileInputStream.read()).thenThrow(new IOException("IO Exception"));

        // Test
        lagoonCLI.main(args);

        // Assertions
        assertEquals("Error while initializing Lagoon:\nI/O error: IO Exception\n", errContent.toString());
    }

    @Test
    public void testMain_withLagoonException() throws IOException, AuthenticationMissingException, AuthenticationException, LagoonException {
        // Set up
        System.setErr(new PrintStream(errContent));
        String[] args = {"property_file_path"};

        LagoonCLI lagoonCLI = new LagoonCLI();

        FileInputStream fileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withAnyArguments().thenReturn(fileInputStream);
        when(fileInputStream.read()).thenThrow(new LagoonException("Lagoon exception"));

        // Test
        lagoonCLI.main(args);

        // Assertions
        assertEquals("Error while initializing Lagoon:\nLagoon exception\n", errContent.toString());
    }
}
