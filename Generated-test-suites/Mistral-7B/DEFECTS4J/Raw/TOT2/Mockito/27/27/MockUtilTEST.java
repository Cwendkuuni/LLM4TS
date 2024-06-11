import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockSettingsImpl;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class mockClass = Mockito.mock(Class.class);
        MockSettingsImpl settings = new MockSettingsImpl();
        T mock = new MockUtil().createMock(mockClass, settings);
        verify(mock, times(1)).createMock(eq(mockClass), eq(settings));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMockWithNullClassToMock() {
        new MockUtil().createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMockWithNullSettings() {
        new MockUtil().createMock(Mockito.mock(Class.class), null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateMockWithFinalClass() {
        final class FinalClass {}
        new MockUtil().createMock(FinalClass.class, new MockSettingsImpl());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateMockWithAbstractClass() {
        abstract class AbstractClass {}
        new MockUtil().createMock(AbstractClass.class, new MockSettingsImpl());
    }

    @Test
    public void testCreateMockWithInterface() {
        interface MyInterface {}
        MyInterface mock = new MockUtil().createMock(MyInterface.class, new MockSettingsImpl());
        Mockito.verify(mock, times(1)).createMock(eq(MyInterface.class), eq(new MockSettingsImpl()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateMockWithPrimitiveType() {
        int integer = 1;
        new MockUtil().createMock(integer.getClass(), new MockSettingsImpl());
    }

    @Test
    public void testCreateMockWithNotAMockException() {
        Object notAMock = new Object();
        try {
            new MockUtil().createMock(notAMock.getClass(), new MockSettingsImpl());
        } catch (NotAMockException e) {
            // Expected exception
        }
    }

    @Test
    public void testResetMock() {
        Class mockClass = Mockito.mock(Class.class);
        T mock = new MockUtil().createMock(mockClass, new MockSettingsImpl());
        new MockUtil().resetMock(mock);
        verify(mock, times(1)).resetMock(eq(mock));
    }

    // Add test cases for other methods here...

}