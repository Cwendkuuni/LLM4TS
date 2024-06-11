import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.eq(invocation))).thenReturn("Mock Value");
        Object result = returnsEmptyValues.answer(invocation);
        verify(returnsEmptyValues).answer(invocation);
        Assert.assertEquals("Mock Value", result);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerMethodNullInvocation() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        InvocationOnMock nullInvocation = null;
        returnsEmptyValues.answer(nullInvocation);
    }

    @Test
    public void testReturnValueForCustomCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        MyCustomCollection customCollection = new MyCustomCollection();
        Assert.assertTrue(customCollection.isEmpty());
        Assert.assertEquals(returnsEmptyValues.returnValueFor(customCollection.getClass()), customCollection);
        assertTrue(customCollection.isEmpty());
    }

    @Test
    public void testReturnValueForNullType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Assert.assertNull(returnsEmptyValues.returnValueFor(null));
    }

    @Test
    public void testReturnValueForNonPrimitiveType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String testString = "Test String";
        Assert.assertEquals(testString, returnsEmptyValues.returnValueFor(String.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReturnValueForModifiableCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        ArrayList<Object> testList = new ArrayList<>();
        testList.add("Test Value");
        Assert.assertFalse(testList.isEmpty());
        Assert.assertEquals(returnsEmptyValues.returnValueFor(ArrayList.class), testList);
        testList.add("Another Test Value");
        Assert.assertFalse(testList.isEmpty());
    }

    @Test
    public void testReturnValueForNullArgument() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Assert.assertNull(returnsEmptyValues.returnValueFor(null));
    }
}