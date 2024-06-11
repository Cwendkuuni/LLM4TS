import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class MainTest {

    @Test
    public void testMain() {
        // Test the main method, no need for assert statement since it should run indefinitely
        Main.main(new String[]{});
    }

    @Test
    public void testProcessInstruction() {
        // Setup
        Main.TOTAL_INSTRUCTIONS = 3;
        Main.instruction = new String[]{
            "out Hello",
            "in",
            "halt"
        };

        // Test processInstruction method for each instruction
        Main.processInstruction(Main.instruction[0]);
        assertEquals("Hello\n", outContent.toString());
        Main.processInstruction(Main.instruction[1]);
        assertEquals(expectedInput, inContent.toString());
        Main.processInstruction(Main.instruction[2]);

        // Verify that the program exits
        assertTrue(systemExitCalled);
    }
    
    @Test
    public void testGetFileName() {
        // Setup - Simulate user input
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("C:\\test.txt".getBytes());
        System.setIn(in);

        // Test getFileName method
        String fileName = Main.getFileName();
        assertEquals("C:\\test.txt", fileName);

        // Restore System.in
        System.setIn(sysInBackup);
    }
    
    @Test
    public void testLoadInstructions() {
        // Setup - Create a temporary test file
        try {
            File tempFile = File.createTempFile("temp", ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write("out Hello");
            writer.newLine();
            writer.write("halt");
            writer.newLine();
            writer.close();

            // Test loadInstructions method
            assertTrue(Main.loadInstructions(tempFile.getAbsolutePath()));

            // Cleanup - Delete the temporary test file
            tempFile.delete();
        } catch (IOException e) {
            fail("Failed to create a temporary test file. Error: " + e.getMessage());
        }
    }
    
    @Test
    public void testRemoveComment() {
        // Test removeComment method
        assertEquals("out Hello", Main.removeComment("out Hello ; this is a comment"));
        assertEquals("out Hello", Main.removeComment("out Hello\";\" this is a comment"));
        assertEquals("out Hello", Main.removeComment("\"out\" ; \"Hello\" this is a comment"));
    }

    // more test methods for other public methods...

    // Additional Test Cases for Edge Cases and Exception Handling
    // ...

    // Variables to capture System.out and System.in streams
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream inContent = new ByteArrayOutputStream();

    // Variable to track if System.exit() was called
    private boolean systemExitCalled = false;

    // Variable to simulate user input
    private final String expectedInput = "5";

    @Before
    public void setUp() {
        // Redirect System.out and System.in to capture their content
        System.setOut(new PrintStream(outContent));
        System.setIn(new ByteArrayInputStream(expectedInput.getBytes()));
        // Track System.exit() calls
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
                if (perm.getName().startsWith("exitVM")) {
                    throw new SecurityException();
                }
            }

            @Override
            public void checkExit(int status) {
                systemExitCalled = true;
                throw new SecurityException();
            }
        });
    }

    @After
    public void tearDown() {
        // Restore System.out and System.in
        System.setOut(System.out);
        System.setIn(System.in);
        // Reset the captured content
        outContent.reset();
        inContent.reset();
        // Reset the systemExitCalled flag
        systemExitCalled = false;
    }
}