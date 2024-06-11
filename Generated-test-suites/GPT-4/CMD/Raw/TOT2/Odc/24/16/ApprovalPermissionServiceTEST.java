package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService approvalPermissionService;

    @Mock
    UserService userService;
    
    @Mock
    ResourceRoleService resourceRoleService;

    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;

        UserTaskInstanceCandidateEntity entity = mock(UserTaskInstanceCandidateEntity.class);
        when(entity.getUserId()).thenReturn(10L);
        when(entity.getRoleId()).thenReturn(10L);
        when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(Arrays.asList(entity));

        Set<User> users = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(2, users.size());
    }

    // Similar test cases for other methods
    // Each method should have multiple tests to cover different scenarios including edge cases and exceptions

}