package com.oceanbase.odc.service.schedule.job;

import static org.junit.AssertassertTrue;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobExecutionContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.shared.constant.TaskStatus;
import com.oceanbase.odc.metadb.schedule.ScheduleTaskEntity;
import com.oceanbase.odc.service.dlm.model.DlmTask;
import com.oceanbase.odc.service.dlm.utils.DlmJobIdUtil;
import com.oceanbase.odc.service.schedule.model.DataArchiveClearParameters;

import java.util.ArrayList;
import java.util.Optional;

public class DataArchiveDeleteJobTest {
   
    @Mock
    JobExecutionContext context;

    @Mock 
    ScheduleTaskEntity taskEntity;

    @Mock 
    DataArchiveClearParameters dataArchiveClearParameters;

    @Test
    public void testExecute() {

      Mockito.when(context.getResult()).thenReturn(taskEntity);
      Mockito.when(taskEntity.getParametersJson()).thenReturn("{\"dataArchiveTaskId\":" + "\"1\"}");

      Optional<ScheduleTaskEntity> dataArchiveTaskOption = Optional.of(new ScheduleTaskEntity());
      
      // set the dataArchiveTaskOption and other dependencies as needed...

      DataArchiveDeleteJob dataArchiveDeleteJob = new DataArchiveDeleteJob();
      dataArchiveDeleteJob.execute(context);

      // add checks to assert that the expected behavior occurred...
    }
}
