package com.oceanbase.odc.service.flow;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;
    
    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCandidates(){
        UserTaskInstanceCandidateEntity candidateEntity=new UserTaskInstanceCandidateEntity();
        candidateEntity.setUserId(1L);
        candidateEntity.setRoleId(2L);
        candidateEntity.setResourceRoleIdentifier("identifier");
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(Mockito.anyLong())).thenReturn(Arrays.asList(candidateEntity));

        Set<User> candidates = approvalPermissionService.getCandidates(1L, true);
        Assert.assertNotNull(candidates);
        Assert.assertEquals(3, candidates.size());
    }
    
    // add more test cases for coverage..
           
}