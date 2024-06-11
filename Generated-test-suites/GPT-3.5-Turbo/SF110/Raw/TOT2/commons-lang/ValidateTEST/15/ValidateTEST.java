import org.junit.Test;

public class ValidateTest {

    @Test
    public void testIsTrueLong() {
        Validate.isTrue(true, "Test message for long", 10L);
    }
    
    @Test
    public void testNotNull() {
        Assert.assertNotNull(Validate.notNull("Test"));
    }
    
    @Test
    public void testNotEmptyArray() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Validate.notEmpty(new String[0]);
        });
    }
    
    @Test
    public void testNotEmptyCollection() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            Validate.notEmpty(new ArrayList<>());
        });
    }
    
    @Test
    public void testValidIndexArray() {
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> {
            Validate.validIndex(new String[]{"Test"}, 1);
        });
    }

}