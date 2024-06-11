import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@SpringJUnitConfig(classes = UserTaskInstanceRepositoryTest.TestConfig.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Given
        FlowNodeStatus status = FlowNodeStatus.APPROVED;
        List<UserTaskInstanceEntity> expectedUserTaskInstanceEntities = Arrays.asList(new UserTaskInstanceEntity());

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findByStatus(status);

        // Then
        assertEquals(expectedUserTaskInstanceEntities, actualUserTaskInstanceEntities);
    }

    // Add test case for findByCandidateUserIdOrRoleIds method here

    // Add test case for findByCandidateUserIdOrResourceRoleIdentifier method here

    // Add test case for findByResourceRoleIdentifierIn method here

    // Add test case for findByRoleId method here

    // Add test case for findByCandidateUserId method here

    // Add test case for update method here

    // Add test case for updateUserTaskIdById method here

    // Add test case for updateStatusById method here

    // Add test case for deleteByFlowInstanceId method here

    // Add test case for findByInstanceTypeAndActivityId method here

    // Add test case for findByInstanceTypeAndName method here

    // Add test case for findConfirmById method here

    // Add test case for findApprovalInstanceIdByFlowInstanceIdAndStatus method here

    @ParameterizedTest(name = "Test findByFlowInstanceIdAndStatus with different flowInstanceIds and statuses : {0} and {1}")
    @MethodSource("getTestDataForFindByFlowInstanceIdAndStatus")
    public void testFindByFlowInstanceIdAndStatus(Long flowInstanceId, FlowNodeStatus status) {
        // Given

        // When
        List<UserTaskInstanceEntity> actualUserTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singletonList(flowInstanceId), status);

        // Then
        assertEquals(1, actualUserTaskInstanceEntities.size());
    }

    static Object[] getTestDataForFindByFlowInstanceIdAndStatus() {
        return new Object[][]{
                {1L, FlowNodeStatus.APPROVED},
                {2L, FlowNodeStatus.REJECTED},
                {3L, FlowNodeStatus.APPROVED},
                {4L, FlowNodeStatus.APPROVED},
                {5L, FlowNodeStatus.APPROVED},
        };
    }

    @Test
    public void testExceptionHandling() {
        // Add test cases for exception handling here, e.g., testing null or empty parameters, unsupported data types, etc.
    }

    static class TestConfig {
        // Add any necessary configuration here, e.g., setting up mocks, creating test data, etc.
    }
}