import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] args = new Object[]{new String("test")};
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.spy(new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] args) {
                return null;
            }
        });
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        assertThat(invocation.getMock(), is(sameInstance(mock)));
        assertThat(invocation.getMethod(), is(sameInstance(mockitoMethod)));
        assertThat(invocation.arguments, is(sameInstance(args)));
        assertThat(invocation.sequenceNumber, is(sequenceNumber));
        assertThat(invocation.realMethod, is(sameInstance(realMethod)));
    }
}