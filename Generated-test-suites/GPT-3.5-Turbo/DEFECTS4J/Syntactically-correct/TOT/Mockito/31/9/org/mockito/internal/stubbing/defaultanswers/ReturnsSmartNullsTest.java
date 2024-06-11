import org.junit.Assert;
import org.junit.Test;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_validReturnType() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return getClass().getMethod("testAnswer_validReturnType");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public ObjectAnswer<?> getAnswer() {
                return null;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        };
        
        Object result = returnsSmartNulls.answer(invocation);
        
        Assert.assertNull(result);
    }

    @Test
    public void testAnswer_canImposterise() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return getClass().getMethod("testAnswer_canImposterise");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public ObjectAnswer<?> getAnswer() {
                return null;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        };
        
        Object result = returnsSmartNulls.answer(invocation);
        
        Assert.assertNull(result);
    }

    @Test
    public void testAnswer_returnSmartNulls() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return getClass().getMethod("testAnswer_returnSmartNulls");
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public ObjectAnswer<?> getAnswer() {
                return null;
            }

            @Override
            public void markVerified() {

            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }
        };
        
        Object result = returnsSmartNulls.answer(invocation);
        
        Assert.assertNull(result);
    }
}