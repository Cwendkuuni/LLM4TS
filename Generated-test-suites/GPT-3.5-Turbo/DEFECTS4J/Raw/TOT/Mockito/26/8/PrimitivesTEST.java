import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitivesTest {

    @Test
    public void test_primitiveTypeOf_booleanPrimitiveType() {
        Class<Boolean> expected = boolean.class;
        Class<Boolean> actual = Primitives.primitiveTypeOf(Boolean.TYPE);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_primitiveTypeOf_integerObject() {
        Class<Integer> expected = int.class;
        Class<Integer> actual = Primitives.primitiveTypeOf(Integer.class);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_isPrimitiveWrapper_booleanPrimitiveType() {
        boolean expected = true;
        boolean actual = Primitives.isPrimitiveWrapper(Boolean.class);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_isPrimitiveWrapper_integerPrimitiveType() {
        boolean expected = false;
        boolean actual = Primitives.isPrimitiveWrapper(int.class);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_primitiveWrapperOf_booleanPrimitiveType() {
        Boolean expected = false;
        Boolean actual = Primitives.primitiveWrapperOf(boolean.class);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_primitiveWrapperOf_integerWrapperType() {
        Integer expected = 0;
        Integer actual = Primitives.primitiveWrapperOf(Integer.class);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_primitiveValueOrNullFor_booleanPrimitiveType() {
        boolean expected = false;
        boolean actual = Primitives.primitiveValueOrNullFor(boolean.class);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_primitiveValueOrNullFor_integerWrapperType() {
        Integer expected = null;
        Integer actual = Primitives.primitiveValueOrNullFor(Integer.class);
        assertEquals(expected, actual);
    }
    
}