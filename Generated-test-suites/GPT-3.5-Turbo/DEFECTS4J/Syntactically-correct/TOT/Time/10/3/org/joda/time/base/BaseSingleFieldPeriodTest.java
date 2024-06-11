import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BaseSingleFieldPeriodTest {

    @Test
    public void between_ReadableInstant_WhenValidInputs_ShouldReturnPeriod() {
        //Arrange
        ReadableInstant start = mock(ReadableInstant.class);
        ReadableInstant end = mock(ReadableInstant.class);
        DurationFieldType field = DurationFieldType.days();

        //Act
        int result = BaseSingleFieldPeriod.between(start, end, field);

        //Assert
        Assert.assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadableInstant_WhenStartIsNull_ShouldThrowIllegalArgumentException() {
        //Arrange
        ReadableInstant start = null;
        ReadableInstant end = mock(ReadableInstant.class);
        DurationFieldType field = DurationFieldType.days();

        //Act
        BaseSingleFieldPeriod.between(start, end, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadableInstant_WhenEndIsNull_ShouldThrowIllegalArgumentException() {
        //Arrange
        ReadableInstant start = mock(ReadableInstant.class);
        ReadableInstant end = null;
        DurationFieldType field = DurationFieldType.days();

        //Act
        BaseSingleFieldPeriod.between(start, end, field);
    }

    @Test
    public void between_ReadablePartial_WhenValidInputs_ShouldReturnPeriod() {
        //Arrange
        ReadablePartial start = mock(ReadablePartial.class);
        ReadablePartial end = mock(ReadablePartial.class);
        ReadablePeriod zeroInstance = mock(ReadablePeriod.class);

        //Act
        int result = BaseSingleFieldPeriod.between(start, end, zeroInstance);

        //Assert
        Assert.assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadablePartial_WhenStartIsNull_ShouldThrowIllegalArgumentException() {
        //Arrange
        ReadablePartial start = null;
        ReadablePartial end = mock(ReadablePartial.class);
        ReadablePeriod zeroInstance = mock(ReadablePeriod.class);

        //Act
        BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadablePartial_WhenEndIsNull_ShouldThrowIllegalArgumentException() {
        //Arrange
        ReadablePartial start = mock(ReadablePartial.class);
        ReadablePartial end = null;
        ReadablePeriod zeroInstance = mock(ReadablePeriod.class);

        //Act
        BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadablePartial_WhenDifferentFieldTypes_ShouldThrowIllegalArgumentException() {
        //Arrange
        ReadablePartial start = mock(ReadablePartial.class);
        ReadablePartial end = mock(ReadablePartial.class);
        when(start.getFieldType(0)).thenReturn(DurationFieldType.days());
        when(end.getFieldType(0)).thenReturn(DurationFieldType.hours());
        ReadablePeriod zeroInstance = mock(ReadablePeriod.class);

        //Act
        BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void between_ReadablePartial_WhenNonContiguousPeriod_ShouldThrowIllegalArgumentException() {
        //Arrange
        ReadablePartial start = mock(ReadablePartial.class);
        ReadablePartial end = mock(ReadablePartial.class);
        when(DateTimeUtils.isContiguous(start)).thenReturn(false);
        ReadablePeriod zeroInstance = mock(ReadablePeriod.class);

        //Act
        BaseSingleFieldPeriod.between(start, end, zeroInstance);
    }

    @Test
    public void standardPeriodIn_WhenValidPeriod_ShouldReturnStandardPeriod() {
        //Arrange
        ReadablePeriod period = mock(ReadablePeriod.class);
        long millisPerUnit = 1000L;

        //Act
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);

        //Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void standardPeriodIn_WhenPeriodIsNull_ShouldReturnZero() {
        //Arrange
        ReadablePeriod period = null;
        long millisPerUnit = 1000L;

        //Act
        int result = BaseSingleFieldPeriod.standardPeriodIn(period, millisPerUnit);

        //Assert
        Assert.assertEquals(0, result);
    }


}