package com.oceanbase.odc.service.flow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private UserService userService;
    
    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    
    @Mock
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;

        UserTaskInstanceCandidateEntity entity = new UserTaskInstanceCandidateEntity();
        entity.setApprovalInstanceId(approvalInstanceId);
        entity.setUserId(1L);
        entity.setRoleId(1L);
        
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Arrays.asList(
                entity));
        
        Set<Long> expectedUserIds = new HashSet<>(Arrays.asList(1L));
        Set<Long> expectedRoleIds = new HashSet<>(Arrays.asList(1L));
        
        assertEquals(expectedUserIds, approvalPermissionService.getCandidates(approvalInstanceId, true));
        assertEquals(expectedRoleIds, approvalPermissionService.getCandidates(approvalInstanceId, false));
        
        verify(userTaskCandidateRepository, times(2)).findByApprovalInstanceId(approvalInstanceId);
    }
    
    // The above method can serve as an example for the rest of the methods and can be adjusted to each method's needs. 
    // The key to writing robust unit test is mocking out all dependencies and testing the functionality of the class itself.
    // Therefore, we use the @Mock annotation to mock out the dependencies of ApprovalPermissionService. 
    // Then with the @InjectMocks annotation, we create an instance of ApprovalPermissionService and inject our mocks into it.
    // In the setup() method, we initialize our mocks with MockitoAnnotations.initMocks(this).
    // We then stub the functionality of any method we may care to test in ApprovalPermissionService.
    // For example, after stubbing the findByApprovalInstanceId() method of UserTaskInstanceCandidateRepository with a predefined return, 
    // we can then use these returned objects to perform assertions against the methods of ApprovalPermissionService.
}
