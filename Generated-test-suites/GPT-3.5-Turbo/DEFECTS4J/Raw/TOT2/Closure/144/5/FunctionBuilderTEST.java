import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionBuilderTest {

   @Test
   public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String name = "testName";
        FunctionType function = builder.withName(name).build();
        assertEquals(name, function.getName());
    }

   @Test
    public void testWithReturnType() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        JSType returnType = new JSType();
        FunctionType function = builder.withReturnType(returnType).build();
        assertEquals(returnType, function.getReturnType());
    }

   @Test
    public void testForConstructor() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType function = builder.forConstructor().build();
        assertTrue(function.isConstructor());
    }

   @Test
    public void testBuild() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        FunctionType function = builder.build();
        assertNotNull(function);
    }

    // Add more test cases for other methods and edge cases

}