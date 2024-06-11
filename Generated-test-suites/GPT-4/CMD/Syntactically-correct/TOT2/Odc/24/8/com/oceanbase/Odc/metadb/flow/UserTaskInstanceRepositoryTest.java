package com.oceanbase.odc.metadb.flow;
/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class UserTaskInstanceRepositoryTest {
    @InjectMocks
    UserTaskInstanceRepository repository;

    @Mock
    FlowNodeStatus status;

    @Mock
    List<UserTaskInstanceEntity> taskEntities;

    @Before
    public void setup() { 
        MockitoAnnotations.initMocks(this); 
    }

    @Test
    public void testFindByStatus() {
        when(repository.findByStatus(any(FlowNodeStatus.class))).thenReturn(taskEntities);
        List<UserTaskInstanceEntity> result = repository.findByStatus(status);
        assertNotNull(result);
        verify(repository).findByStatus(status);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByStatus_NullStatus() {
        repository.findByStatus(null);
    }

    // similarly for other methods
}