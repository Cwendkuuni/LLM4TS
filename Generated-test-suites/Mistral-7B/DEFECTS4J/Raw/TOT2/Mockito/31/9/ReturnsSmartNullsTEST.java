import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setUp() {
        returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
    }

    @Test
    public void testAnswer() {
        Object expectedReturnValue = new Object();
        Method method = Mockito.method(Mockito.class, "methodName");
        Mockito.doReturn(expectedReturnValue).when(returnsSmartNulls).answer(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.eq(method));

        Object actualReturnValue = returnsSmartNulls.answer(Mockito.mock(Object.class));

        Assert.assertEquals(expectedReturnValue, actualReturnValue);
    }
}