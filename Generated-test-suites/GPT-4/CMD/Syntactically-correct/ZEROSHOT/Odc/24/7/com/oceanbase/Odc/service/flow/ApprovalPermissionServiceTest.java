
package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

import java.util.*;
import java.util.stream.Collectors;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    ApprovalPermissionService service;

    @Mock
    UserRepository userRepository;
    @Mock
    UserTaskInstanceCandidateRepository userTaskCandidateRepository;
    @Mock
    UserTaskInstanceRepository userTaskInstanceRepository;
    @Mock
    UserService userService;
    @Mock
    ResourceRoleService resourceRoleService;
    @Mock
    AuthenticationFacade authenticationFacade;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(service, "userService", userService);
        ReflectionTestUtils.setField(service, "resourceRoleService", resourceRoleService);
        ReflectionTestUtils.setField(service, "authenticationFacade", authenticationFacade);
    }

    @Test
    public void testGetCandidates() {
      //all possible setup for the method call and assumptions
        doReturn(Arrays.asList(mock(UserEntity.class)))
                .when(userRepository)
                .findByUserIdsOrRoleIds(anySet(), anySet(), eq(true));

        doReturn(Arrays.asList(mock(UserTaskInstanceCandidateEntity.class)))
                .when(userTaskCandidateRepository)
                .findByApprovalInstanceId(anyLong());

        Set<User> result = service.getCandidates(1L, true);

        verify(userRepository, times(1)).findByUserIdsOrRoleIds(anySet(), anySet(), eq(true));
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(anyLong());

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
    }

    //similarly add other test methods here for each public method in your class
    //follow the pattern above:
    // 1. setup any data or mocks needed for the test
    // 2. execute the method under test
    // 3. validate the results (using asserts) or interactions (verify on mocks)

}
