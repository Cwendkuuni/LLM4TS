import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class JSJshopTest {

    private JSJshop jSJshop;

    @Before
    public void setUp() {
        jSJshop = new JSJshop();
    }

    @Test
    public void testMain_InvalidArgs() {
        String[] args = {};
        JSUtil.println("Usage :\njava JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
                        "[number-of-plans] can be 'one' or 'all'. The default value is 'one'");
        JSUtil.println("Invalid parameter");
        assertNull(JSUtil.capture(() -> JSJshop.main(args)));
    }

    @Test
    public void testMain_OnePlan() {
        String[] args = {"domainDefFile", "problemDefFile", "one"};
        JSJshopVars.allPlans = false;
        assertDoesNotThrow(() -> JSJshop.main(args));
        assertFalse(JSJshopVars.allPlans);
    }

    @Test
    public void testMain_AllPlans() {
        String[] args = {"domainDefFile", "problemDefFile", "all"};
        JSJshopVars.allPlans = false;
        assertDoesNotThrow(() -> JSJshop.main(args));
        assertTrue(JSJshopVars.allPlans);
    }

    @Test
    public void testParserFile_ValidFile() {
        JSListPlanningProblem expectedProbSet = new JSListPlanningProblem();
        JSPlanningProblem expectedProb = new JSPlanningProblem();
        expectedProbSet.addElement(expectedProb);
        jSJshop.parserFile("nameProblemFile");
        JSListPlanningProblem actualProbSet = jSJshop.probSet();
        assertEquals(expectedProbSet.size(), actualProbSet.size());
        assertEquals(expectedProb, actualProbSet.elementAt(0));
    }

    @Test
    public void testParserFile_InvalidFile() {
        assertFalse(jSJshop.parserFile("invalidFile"));
    }

    @Test
    public void testGetBufferedReader_ValidApplet() {
        JApplet applet = mock(JApplet.class);
        String dir = ".";
        String file = "file";
        jSJshop.getBufferedReader(dir, file, applet);
        verify(applet, times(1)).getCodeBase();
        verifyNoMoreInteractions(applet);
    }

    @Test
    public void testGetBufferedReader_InvalidFile() {
        String dir = ".";
        String file = null;
        assertNull(jSJshop.getBufferedReader(dir, file));
    }

    @Test
    public void testProcessToken_DefDomain() {
        StreamTokenizer tokenizer = mock(StreamTokenizer.class);
        when(tokenizer.lineno()).thenReturn(1);
        when(JSUtil.readWord(tokenizer, "JSJshop>>processToken")).thenReturn("defDomain");
        
        assertDoesNotThrow(() -> jSJshop.processToken(tokenizer));
        verify(tokenizer, times(1)).pushBack();
        verify(tokenizer, times(1)).lowerCaseMode(true);
        verify(tokenizer, never()).initParseTable();
        verifyNoMoreInteractions(tokenizer);
    }

    @Test
    public void testProcessToken_DefProblem() {
        StreamTokenizer tokenizer = mock(StreamTokenizer.class);
        when(tokenizer.lineno()).thenReturn(1);
        when(JSUtil.readWord(tokenizer, "JSJshop>>processToken")).thenReturn("defProblem");
        
        assertDoesNotThrow(() -> jSJshop.processToken(tokenizer));
        verify(tokenizer, times(1)).pushBack();
        verify(tokenizer, times(1)).lowerCaseMode(true);
        verify(tokenizer, never()).initParseTable();
        verifyNoMoreInteractions(tokenizer);
    }

    @Test
    public void testProcessToken_InvalidToken() {
        StreamTokenizer tokenizer = mock(StreamTokenizer.class);
        when(tokenizer.lineno()).thenReturn(1);
        when(JSUtil.readWord(tokenizer, "JSJshop>>processToken")).thenReturn("invalidToken");
        
        assertThrows(JSParserError.class, () -> {
            jSJshop.processToken(tokenizer);
        });
        verify(tokenizer, times(1)).pushBack();
        verify(tokenizer, times(1)).lowerCaseMode(true);
        verify(tokenizer, never()).initParseTable();
        verifyNoMoreInteractions(tokenizer);
    }

}