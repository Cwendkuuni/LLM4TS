import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationField;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.LocalDateTime;
import org.joda.time.Duration;

import static org.junit.Assert.assertEquals;

public class UnsupportedDurationFieldTest {

    @Test
    public void testUnsupportedDurationField() {
        DurationField unsupportedField = UnsupportedDurationField.getInstance(DurationFieldType. seconds());
         // Test getters
         assertEquals("Seconds field", DurationFieldType. seconds(), unsupportedField.getType());
         assertEquals("Seconds", "seconds", unsupportedField.getName());
         assertEquals(false, unsupportedField.isSupported());
         assertEquals(true, unsupportedField.isPrecise());

         // Test methods that throw UnsupportedOperationException
         try {
             unsupportedField.getValue(Duration.standardDays(1).getMillis());
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.getValueAsLong(Duration.standardDays(1).getMillis());
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.getValue(Duration.standardDays(1).getMillis(), ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()));
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.getValueAsLong(Duration.standardDays(1).getMillis(), ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()));
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.getMillis(DurationField.ONE);
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.getMillis(Duration.standardDays(1).getMillis());
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.getMillis(Duration.standardDays(1).getMillis(), ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()));
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.getMillis(Duration.standardDays(1).getMillis(), ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()));
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.add(ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()), Duration.standardDays(1).getMillis());
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             unsupportedField.add(ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()), Duration.standardDays(1));
         } catch (UnsupportedOperationException e) {
             // expected
         }

         try {
             int difference = unsupportedField.getDifference(ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().minusDays(1).toDate()), ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()));
             assertEquals(0, difference);
         } catch (Exception e) {
             // Unexpected exception
         }

         try {
             long differenceAsLong = unsupportedField.getDifferenceAsLong(ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().minusDays(1).toDate()), ISOChronology.getInstanceUTC().getMillis(LocalDateTime.now().toDate()));
             assertEquals(0, differenceAsLong);
         } catch (Exception e) {
             // Unexpected exception
         }
    }
}