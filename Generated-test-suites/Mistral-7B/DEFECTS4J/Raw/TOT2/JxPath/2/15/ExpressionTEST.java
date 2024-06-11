import static org.junit.Assert.assertTrue;

public class ExpressionTest {
    // ...

    @Test
    public void testIsContextDependent() {
        MyExpression myExpression = new MyExpression(); // Replace MyExpression with the actual subclass name
        assertTrue(myExpression.isContextDependent()); // Replace with the expected result based on subclass's implementation
    }
}