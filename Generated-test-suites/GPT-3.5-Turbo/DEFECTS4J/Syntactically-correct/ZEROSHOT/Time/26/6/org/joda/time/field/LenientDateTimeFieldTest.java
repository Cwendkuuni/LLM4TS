import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField fieldMock;
    private Chronology chronologyMock;

    @Before
    public void setUp() {
        fieldMock = mock(DateTimeField.class);
        chronologyMock = mock(Chronology.class);
        lenientDateTimeField = new LenientDateTimeField(fieldMock, chronologyMock);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1000;
        int value = 5;
        long expected = 2000;
        
        when(chronologyMock.getZone().convertUTCToLocal(instant)).thenReturn(1000);
        when(fieldMock.get(instant)).thenReturn(0);
        when(fieldMock.getType()).thenReturn(fieldMock);
        when(chronologyMock.withUTC()).thenReturn(chronologyMock);
        when(fieldMock.add(1000, value)).thenReturn(2000);
        when(chronologyMock.getZone().convertLocalToUTC(expected, false)).thenReturn(3000);
        
        long result = lenientDateTimeField.set(instant, value);
        
        assertEquals(expected, result);
    }

}