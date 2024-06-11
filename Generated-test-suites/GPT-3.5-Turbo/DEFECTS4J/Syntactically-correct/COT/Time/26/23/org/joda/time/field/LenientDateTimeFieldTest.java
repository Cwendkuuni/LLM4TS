import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField wrappedField;
    private Chronology base;

    @Before
    public void setUp() {
        wrappedField = createMock(DateTimeField.class);
        base = createMock(Chronology.class);
        lenientDateTimeField = new LenientDateTimeField(wrappedField, base);
    }

    @Test
    public void testGetInstance() {
        DateTimeField field = createMock(DateTimeField.class);
        Chronology base = createMock(Chronology.class);
        
        // Test when field is null
        assertNull(LenientDateTimeField.getInstance(null, base));
        
        // Test when field is lenient
        expect(field.isLenient()).andReturn(true);
        replay(field);
        assertSame(field, LenientDateTimeField.getInstance(field, base));
        verify(field);
        
        // Test when field is strict and already wrapped
        DateTimeField wrappedField = createMock(DateTimeField.class);
        expect(field.isLenient()).andReturn(false);
        expect(field instanceof StrictDateTimeField).andReturn(true);
        expect(((StrictDateTimeField) field).getWrappedField()).andReturn(wrappedField);
        replay(field);
        assertSame(wrappedField, LenientDateTimeField.getInstance(field, base));
        verify(field);
        
        // Test when field is strict and not already wrapped
        expect(field.isLenient()).andReturn(false);
        expect(field instanceof StrictDateTimeField).andReturn(false);
        replay(field);
        assertNotNull(LenientDateTimeField.getInstance(field, base));
        verify(field);
    }

    @Test
    public void testIsLenient() {
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        // Test with safeSubtract returning positive difference
        long instant = 1000;
        int value = 10;
        expect(wrappedField.get(instant)).andReturn(5);
        expect(base.getZone()).andReturn(createMock(DateTimeZone.class));
        expect(base.getZone().convertUTCToLocal(instant)).andReturn(2000L);
        expect(wrappedField.getType()).andReturn(createMock(DateTimeFieldType.class));
        expect(base.withUTC()).andReturn(createMock(Chronology.class));
        expect(wrappedField.getType().getField(base.withUTC())).andReturn(createMock(DateTimeField.class));
        expect(base.withUTC()).andReturn(createMock(Chronology.class));
        expect(base.getZone().convertLocalToUTC(2005, false)).andReturn(5000L); // Add the difference between 10 and 5 to localInstant
        replay(wrappedField, base);
        assertEquals(5000L, lenientDateTimeField.set(instant, value));
        verify(wrappedField, base);
        
        // Test with safeSubtract returning negative difference
        instant = 2000L;
        value = 5;
        expect(wrappedField.get(instant)).andReturn(10);
        expect(base.getZone()).andReturn(createMock(DateTimeZone.class));
        expect(base.getZone().convertUTCToLocal(instant)).andReturn(5000L);
        expect(wrappedField.getType()).andReturn(createMock(DateTimeFieldType.class));
        expect(base.withUTC()).andReturn(createMock(Chronology.class));
        expect(wrappedField.getType().getField(base.withUTC())).andReturn(createMock(DateTimeField.class));
        expect(base.withUTC()).andReturn(createMock(Chronology.class));
        expect(base.getZone().convertLocalToUTC(1995, false)).andReturn(1000L); // Subtract the difference between 5 and 10 from localInstant
        replay(wrappedField, base);
        assertEquals(1000L, lenientDateTimeField.set(instant, value));
        verify(wrappedField, base);
    }
}