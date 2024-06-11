import org.joda.time.DateTimeField;
import org.joda.time.DurationFieldType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class LenientDateTimeFieldTest {

    private LenientDateTimeField lenientDateTimeField;
    private DateTimeField dateTimeField;

    @Before
    public void setUp() {
        dateTimeField = mock(DateTimeField.class);
        lenientDateTimeField = new LenientDateTimeField(dateTimeField, null);
    }

    @Test
    public void testIsLenient() {
        when(dateTimeField.isLenient()).thenReturn(true);
        Assert.assertTrue(lenientDateTimeField.isLenient());

        when(dateTimeField.isLenient()).thenReturn(false);
        Assert.assertTrue(lenientDateTimeField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 1234567890;
        int value = 5;
        long localInstant = 9876543210L;
        long difference = 2;
        long convertedLocalInstant = 36925814703L;

        when(lenientDateTimeField.get(instant)).thenReturn(3);
        when(dateTimeField.getType()).thenReturn(DurationFieldType.years());
        when(lenientDateTimeField.iBase.withUTC()).thenReturn(null);
        when(lenientDateTimeField.iBase.getZone()).thenReturn(null);
        when(lenientDateTimeField.iBase.getZone().convertUTCToLocal(instant)).thenReturn(localInstant);
        when(dateTimeField.add(localInstant, difference)).thenReturn(convertedLocalInstant);
        when(lenientDateTimeField.iBase.getZone().convertLocalToUTC(convertedLocalInstant, false)).thenReturn(9999999999L);

        Assert.assertEquals(9999999999L, lenientDateTimeField.set(instant, value));
    }

}