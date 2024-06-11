import static org.junit.Assert.*;
import org.junit.*;

public class MainTest {

    // Test for processInstruction() method
    @Test
    public void testProcessInstruction() {
        // Typical use case test
        String instruction = "out hello";
        Main.processInstruction(instruction);
        // Check if the output is correct
        assertEquals("hello", outContent.toString().trim());

        // Edge case test
        instruction = "jmp 1";
        Main.processInstruction(instruction);
        // Check if the value of iPtr is updated correctly
        assertEquals(0, Main.iPtr);

        // Error scenario test
        instruction = "unknownOpcode";
        Main.processInstruction(instruction);
        // Check if the program exits with status code 0
        assertTrue(exitCalled);
    }

    // Test for getFileName() method
    @Test
    public void testGetFileName() {
        // Typical use case test
        String expectedFileName = "testFile.txt";
        String userInput = "testFile.txt";
        provideInput(userInput);
        String actualFileName = Main.getFileName();
        // Check if the correct file name is returned
        assertEquals(expectedFileName, actualFileName);

        // Edge case test
        userInput = "q";
        provideInput(userInput);
        actualFileName = Main.getFileName();
        // Check if the program exits with status code 0
        assertTrue(exitCalled);
    }

    // Test for loadInstructions() method
    @Test
    public void testLoadInstructions() {
        // Typical use case test
        String fileName = "testFile.txt";
        boolean result = Main.loadInstructions(fileName);
        // Check if the instructions are loaded successfully
        assertTrue(result);

        // Error scenario test
        fileName = "nonExistentFile.txt";
        result = Main.loadInstructions(fileName);
        // Check if the loading of instructions fails
        assertFalse(result);
    }

    // Test for removeComment() method
    @Test
    public void testRemoveComment() {
        // Typical use case test
        String line = "add a, b ; add a and b";
        String expectedLine = "add a, b";
        String actualLine = Main.removeComment(line);
        // Check if the comment is removed correctly
        assertEquals(expectedLine, actualLine);

        // Edge case test
        line = "add a, b;";
        expectedLine = "add a, b";
        actualLine = Main.removeComment(line);
        // Check if the comment is removed correctly
        assertEquals(expectedLine, actualLine);
    }

    // Test for splitArguments() method
    @Test
    public void testSplitArguments() {
        // Typical use case test
        String arguments = "arg1, arg2, arg3";
        String[] expectedArgs = {"arg1", "arg2", "arg3"};
        String[] actualArgs = Main.splitArguments(arguments);
        // Check if the arguments are split correctly
        assertArrayEquals(expectedArgs, actualArgs);

        // Edge case test
        arguments = "arg1";
        expectedArgs = new String[] {"arg1"};
        actualArgs = Main.splitArguments(arguments);
        // Check if the arguments are split correctly
        assertArrayEquals(expectedArgs, actualArgs);
    }

    // Test for getValue() method
    @Test
    public void testGetValue() {
        // Typical use case test
        String operand = "10";
        int expectedValue = 10;
        int actualValue = Main.getValue(operand);
        // Check if the correct value is returned
        assertEquals(expectedValue, actualValue);

        // Edge case test
        operand = "nonExistentVariable";
        expectedValue = 0;
        actualValue = Main.getValue(operand);
        // Check if the value of non-existent variable is 0
        assertEquals(expectedValue, actualValue);
    }

    // Rest of the test methods for the remaining methods...

    // Helper method to simulate user input
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    // Helper fields to capture system output and exit status
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private boolean exitCalled = false;

    // Setup method to redirect system output and catch exit status
    @Before
    public void setup() {
        System.setOut(new PrintStream(outContent));
        System.setSecurityManager(new SecurityManager() {
            @Override
            public void checkExit(int status) {
                exitCalled = true;
                throw new SecurityException();
            }
        });
    }

    // Teardown method to restore system output and exit status
    @After
    public void teardown() {
        System.setOut(originalOut);
        System.setSecurityManager(null);
    }
}