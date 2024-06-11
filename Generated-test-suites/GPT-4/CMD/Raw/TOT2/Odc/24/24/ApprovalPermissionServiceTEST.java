package com.oceanbase.odc.service.flow;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ApprovalPermissionServiceTest {
    @Mock
    private UserService userService;
    
    @Mock
    private AuthenticationFacade authenticationFacade;
    
    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;
    
    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testIsApprovable() {
        
        // Create and save a UserTaskInstanceEntity
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        
        // Mock the return of the getApprovableApprovalInstances() method
        Set<UserTaskInstanceEntity> entities = new HashSet<>();
        entities.add(userTaskInstanceEntity);
        
        // Mock the method calls with specific returns
        when(approvalPermissionService.getApprovableApprovalInstances()).thenReturn(entities);
        
        // Test for an existing approval instance, expecting to be approvable
        assertTrue(approvalPermissionService.isApprovable(1L));
        
        // Test for a non-existing approval instance, expecting not to be approvable
        assertFalse(approvalPermissionService.isApprovable(2L));
    }
}