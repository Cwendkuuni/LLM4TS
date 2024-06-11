import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;

class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetCandidates_Success() {
        // Given valid approvalInstanceId and enabled state

        // When calling getCandidates

        // Then asserts here
    }

    // ... more JUnit 4 tests for each method and edge cases ...

}