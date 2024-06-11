import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class SqlConsoleInterceptorTest {
    private SqlConsoleInterceptor interceptor;
    private AuthenticationFacade mockAuthFacade;

    @Before
    public void setUp() {
        mockAuthFacade = Mockito.mock(AuthenticationFacade.class);
        interceptor = new SqlConsoleInterceptor(mockAuthFacade);
    }

    @Test
    public void testIsIndividualTeam() {
        // Given
        User mockUser = new User("testUser", "testPassword", OrganizationType.INDIVIDUAL);
        given(mockAuthFacade.currentUser()).willReturn(mockUser);

        // When
        boolean result = interceptor.isIndividualTeam();

        // Then
        assertEquals(true, result);
    }
}