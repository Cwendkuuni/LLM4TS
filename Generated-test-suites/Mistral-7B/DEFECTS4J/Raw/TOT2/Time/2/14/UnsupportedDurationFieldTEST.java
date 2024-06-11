import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.days());

        assertSame(unsupportedDurationField1, unsupportedDurationField2);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstanceNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    @Test
    public void testGetInstanceMultipleThreads() {
        final int numThreads = 10;
        final DurationFieldType type = DurationFieldType.days();

        Runnable runnable = () -> UnsupportedDurationField.getInstance(type);
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(runnable);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetInstanceCaching() {
        UnsupportedDurationField unsupportedDurationField1 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField unsupportedDurationField2 = UnsupportedDurationField.getInstance(DurationFieldType.days());
        UnsupportedDurationField unsupportedDurationField3 = UnsupportedDurationField.getInstance(DurationFieldType.days());

        assertSame(unsupportedDurationField1, unsupportedDurationField2);
        assertSame(unsupportedDurationField1, unsupportedDurationField3);
    }
}