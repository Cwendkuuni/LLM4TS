
/*
 * Copyright (c) 2023 OceanBase
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.oceanbase.odc.service.schedule.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.schedule.model.DataArchiveRollbackParameters;
import com.oceanbase.tools.migrator.common.enums.JobType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataArchiveRollbackJobTest {

    @Mock
    private JobExecutionContext jobExecutionContext;

    @Mock
    private ScheduleTaskEntity taskEntity;

    @Test
    public void testExecute() {
        DataArchiveRollbackJob job = new DataArchiveRollbackJob();

        when(jobExecutionContext.getResult()).thenReturn(taskEntity);
        when(taskEntity.getParametersJson()).thenReturn("some parameters");
        when(taskEntity.getResultJson()).thenReturn("result json");
        when(taskEntity.getId()).thenReturn(1L);
        
        DataArchiveRollbackParameters parameters = new DataArchiveRollbackParameters();
        parameters.setDataArchiveTaskId(1);
        when(taskEntity.getParametersJson()).thenReturn(parameters);
        
        ScheduleTaskEntity dataArchiveTask = new ScheduleTaskEntity();
        dataArchiveTask.setId(1L);
        when(job.scheduleTaskRepository.findById(anyLong())).thenReturn(Optional.of(dataArchiveTask));
        
        DlmTask dlmTask = new DlmTask();
        dlmTask.setSourceDatabaseId(1L);
        dlmTask.setTargetDatabaseId(2L);
        when(job.JsonUtils.fromJson(any(), any(TypeReference.class))).thenReturn(Collections.singletonList(dlmTask));
        
        job.execute(jobExecutionContext);

        verify(job.scheduleTaskRepository, times(1)).updateStatusById(anyLong(), any());
    }
}
