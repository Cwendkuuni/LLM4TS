import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.RunWith;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

    @Test
    public void testReturnsSmartNullsInstance() {
        ReturnsSmartNulls instance = new ReturnsSmartNulls();
        assert instance instanceof ReturnsSmartNulls;
    }
}