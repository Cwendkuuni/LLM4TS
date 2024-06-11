package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import numpy.core.tests.test_memmap.TestScalarReshape;

public class UserTaskInstanceRepositoryTest {

  @InjectMocks
  private UserTaskInstanceRepository userTaskInstanceRepository;

  @Mock
  private UserTaskInstanceEntity userTaskInstanceEntity;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }
  
  @Test
  public void testFindByStatus() {
    when(userTaskInstanceRepository.findByStatus(any()))
        .thenReturn(Collections.singletonList(userTaskInstanceEntity));
    List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
    assertEquals(1, result.size());
  }

  //Simliar methods for the rest of the public methods in the UserTaskInstanceRepository
    
}

