import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = UnsupportedDurationField.getInstance(type);
        assertSame(unsupportedField1, unsupportedField2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullType() {
        DurationFieldType type = null;
        UnsupportedDurationField.getInstance(type);
    }

    @Test
    public void testGetInstanceMultipleThreads() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    DurationFieldType type = DurationFieldType.MINUTES;
                    UnsupportedDurationField unsupportedField = UnsupportedDurationField.getInstance(type);
                }
            });
        }
        executor.shutdown();
    }

    @Test
    public void testMultipleInstances() {
        DurationFieldType type = DurationFieldType.MINUTES;
        UnsupportedDurationField unsupportedField1 = UnsupportedDurationField.getInstance(type);
        UnsupportedDurationField unsupportedField2 = new UnsupportedDurationField(type);
        assertSame(unsupportedField1, unsupportedField2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testInvalidMethods() {
        UnsupportedDurationField unsupportedField = new UnsupportedDurationField(DurationFieldType.MINUTES);
        unsupportedField.getValue(1);
    }
}