import org.joda.time.*;
import org.junit.*;

public class LenientDateTimeFieldTest {

    //initialize objects
    private static Chronology chronology;
    private static DateTimeField field;
    private static LenientDateTimeField lenientField;

    @BeforeClass
    public static void setUp() {
        chronology = new Chronology();
        field = new DateTimeField(chronology);
        lenientField = new LenientDateTimeField(field, chronology);
    }

    @Test
    public void testGetInstance() {
        DateTimeField strictField = new StrictDateTimeField(field);
        DateTimeField lenientInstance = LenientDateTimeField.getInstance(strictField, chronology);
        
        Assert.assertNotSame(strictField, lenientInstance);
        Assert.assertTrue(lenientInstance.isLenient());
    }

    @Test
    public void testIsLenient() {
        Assert.assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = 123456789;
        int value = 10;

        long result = lenientField.set(instant, value);

        Assert.assertEquals(instant + (value - field.get(instant)), result);
    }

    //additional test methods for other methods in LenientDateTimeField class

    @AfterClass
    public static void tearDown() {
        chronology = null;
        field = null;
        lenientField = null;
    }

}