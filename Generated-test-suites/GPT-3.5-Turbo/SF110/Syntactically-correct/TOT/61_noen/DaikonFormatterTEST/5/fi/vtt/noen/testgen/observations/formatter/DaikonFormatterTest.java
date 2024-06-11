import org.junit.*;
import java.util.*;
import static org.mockito.Mockito.*;

public class DaikonFormatterTest {

    DaikonFormatter daikonFormatter;

    @Before
    public void setUp() {
        // create a mock interface for InputInterface
        Collection<Class> mockInputs = new ArrayList<>();
        mockInputs.add(InputInterface.class);

        // create a mock interface for OutputInterface
        Collection<Class> mockOutputs = new ArrayList<>();
        mockOutputs.add(OutputInterface.class);

        try {
            daikonFormatter = new DaikonFormatter("testfile", mockInputs);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to create DaikonFormatter object");
        }
    }

    @Test
    public void testConstructorWithSimpleFlagTrue() {
        try {
            DaikonFormatter daikonFormatter = new DaikonFormatter("testfile", true);
            // check that inputs collection is null
            Assert.assertNull(daikonFormatter.inputs);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to create DaikonFormatter object with simple flag true");
        }
    }

    @Test
    public void testConstructorWithSimpleFlagFalse() {
        try {
            // check that inputs collection is not null
            Assert.assertNotNull(daikonFormatter.inputs);
            // check that inputs collection contains InputInterface.class
            Assert.assertTrue(daikonFormatter.inputs.contains(InputInterface.class));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to create DaikonFormatter object with simple flag false");
        }
    }

    @Test
    public void testHeader() {
        String expected = "decl-version 2.0\nvar-comparability implicit";
        String actual = daikonFormatter.header();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFooter() {
        String expected = "";
        String actual = daikonFormatter.footer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFileNameExtension() {
        String expected = "dtrace";
        String actual = daikonFormatter.fileNameExtension();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testObservationsWithSimpleFlagTrue() {
        ProgramRun mockRun = mock(ProgramRun.class);
        Iterator<Event> mockIterator = mock(Iterator.class);
        when(mockRun.iterator()).thenReturn(mockIterator);
        Event mockEvent = mock(Event.class);
        when(mockIterator.hasNext()).thenReturn(true, true, false);
        when(mockIterator.next()).thenReturn(mockEvent);
        when(mockEvent.isExit()).thenReturn(false);
        when(mockEvent.getName()).thenReturn("event1");

        String expected = "event1:::ENTER\n  attribute1\n  attribute2\n";
        String actual = daikonFormatter.observations(mockRun);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testObservationsWithSimpleFlagFalse() {
        ProgramRun mockRun = mock(ProgramRun.class);
        Iterator<Event> mockIterator = mock(Iterator.class);
        when(mockRun.iterator()).thenReturn(mockIterator);
        Event mockEvent1 = mock(Event.class);
        when(mockIterator.hasNext()).thenReturn(true, true, false);
        when(mockIterator.next()).thenReturn(mockEvent1);
        when(mockEvent1.isExit()).thenReturn(false);
        when(mockEvent1.getName()).thenReturn("event1");

        Event mockEvent2 = mock(Event.class);
        when(mockIterator.hasNext()).thenReturn(true, true, false);
        when(mockIterator.next()).thenReturn(mockEvent2);
        when(mockEvent2.isExit()).thenReturn(true);
        when(mockEvent2.getName()).thenReturn("event1_EXIT");

        Collection<String> mockInputs = new ArrayList<>();
        mockInputs.add("event1");

        try {
            DaikonFormatter daikonFormatter = new DaikonFormatter("testfile", mockInputs);
            String expected = "event1:::ENTER\n  attribute1\n  attribute2\n" +
                    "event1:::EXIT1\n  attribute1\n  attribute2\n";
            String actual = daikonFormatter.observations(mockRun);
            Assert.assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to create DaikonFormatter object with inputs collection");
        }
    }

    @Test
    public void testSimplePoint() {
        Event mockEvent = mock(Event.class);
        when(mockEvent.getName()).thenReturn("event1");
        when(mockEvent.isExit()).thenReturn(false);

        OutputBuffer mockOut = mock(OutputBuffer.class);
        daikonFormatter.simplePoint(mockEvent, "10", mockOut);

        verify(mockOut).append("");
        verify(mockOut).append("event1:::ENTER");
        verify(mockOut).append("");
        verify(mockOut).append("attribute1");
        verify(mockOut).append("1");
    }

    @Test
    public void testPoint() {
        Event mockEvent = mock(Event.class);
        when(mockEvent.getName()).thenReturn("event1");
        when(mockEvent.isExit()).thenReturn(false);

        OutputBuffer mockOut = mock(OutputBuffer.class);
        daikonFormatter.point("event1", mockEvent, "11", mockOut);

        verify(mockOut).append("");
        verify(mockOut).append("event1:::ENTER");
        verify(mockOut).append("");
        verify(mockOut).append("attribute1");
        verify(mockOut).append("1");
    }

    @Test
    public void testDeclarationsWhenEventNameAlreadyExists() {
        Event mockEvent = mock(Event.class);
        String eventName = "event1";
        Set<String> mockDeclarations = mock(Set.class);
        when(mockDeclarations.contains(eventName)).thenReturn(true);
        daikonFormatter.declarations(mockEvent, eventName, mockOut, "11");

        verifyZeroInteractions(mockOut);
    }

    @Test
    public void testDeclarationsWhenEventNameDoesNotExist() {
        Event mockEvent = mock(Event.class);
        String eventName = "event1";
        Set<String> mockDeclarations = mock(Set.class);
        when(mockDeclarations.contains(eventName)).thenReturn(false);
        daikonFormatter.declarations(mockEvent, eventName, mockOut, "11");

        verify(mockOut).append("");
        verify(mockOut).append("ppt event1:::ENTER");
        verify(mockOut).append("ppt-type enter");
        verify(mockOut).append("  variable attribute1");
        verify(mockOut).append("    var-kind variable");
        verify(mockOut).append("    dec-type type1");
        verify(mockOut).append("    rep-type type1");
        verify(mockOut).append("    comparability 11");
    }

    @Test
    public void testAttributes() {
        Event mockEvent = mock(Event.class);
        EventAttribute mockAttribute1 = mock(EventAttribute.class);
        when(mockAttribute1.getName()).thenReturn("attribute1");
        when(mockAttribute1.getType()).thenReturn("type1");
        when(mockAttribute1.getValue()).thenReturn("value1");
        EventAttribute mockAttribute2 = mock(EventAttribute.class);
        when(mockAttribute2.getName()).thenReturn("attribute2");
        when(mockAttribute2.getType()).thenReturn("type2");
        when(mockAttribute2.getValue()).thenReturn("value2");
        Iterator<EventAttribute> mockIterator = mock(Iterator.class);
        when(mockEvent.attributes()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(true, true, false);
        when(mockIterator.next()).thenReturn(mockAttribute1, mockAttribute2);

        OutputBuffer mockOut = mock(OutputBuffer.class);
        daikonFormatter.attributes(mockEvent, mockOut);

        verify(mockOut).append("attribute1");
        verify(mockOut).append("value1");
        verify(mockOut).append("1");
        verify(mockOut).append("attribute2");
        verify(mockOut).append("value2");
        verify(mockOut).append("1");
    }

    @After
    public void tearDown() {
        daikonFormatter = null;
    }

}