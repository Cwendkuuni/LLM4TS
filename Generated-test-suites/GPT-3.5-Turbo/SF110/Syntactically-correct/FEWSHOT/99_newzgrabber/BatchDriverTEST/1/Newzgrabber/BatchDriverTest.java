package Newzgrabber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;
    private Ini groupsini;
    private Ini batchini;
    private Hashtable NewsgroupsHash;
    private Hashtable BatchgroupsHash;
    private Hashtable BatchretrieveHash;
    private Hashtable OptionsHash;
    private Vector BatchgroupsVector;
    private String[] subjectlines;

    @Before
    public void setUp() {
        groupsini = mock(Ini.class);
        batchini = mock(Ini.class);
        NewsgroupsHash = new Hashtable();
        BatchgroupsHash = new Hashtable();
        BatchretrieveHash = new Hashtable();
        OptionsHash = new Hashtable();
        BatchgroupsVector = new Vector();
        subjectlines = new String[]{};

        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test
    public void testGetGroups() {
        batchDriver.BatchgroupsVector = BatchgroupsVector;
        assertEquals(BatchgroupsVector.size(), batchDriver.getGroups().length);
    }

    @Test
    public void testGetBatchJob() {
        batchDriver.NewsgroupsHash = NewsgroupsHash;
        batchDriver.BatchgroupsHash = BatchgroupsHash;
        batchDriver.OptionsHash = OptionsHash;

        Newzgrabber.nf = mock(NewsFactory.class);
        NewsFactory.setThreadLimit(1);
        NewsFactory nf = Newzgrabber.nf;
        NNTP news = mock(NNTP.class);
        Newzgrabber.nf.getNewsSocket("server");
        when(nf.getNewsSocket("server")).thenReturn(news);

        assertNotNull(batchDriver.getBatchJob("group"));
    }

    @Test
    public void testGetSubjectLines() {
        batchDriver.subjectlines = subjectlines;
        assertEquals(subjectlines, batchDriver.getSubjectLines());
    }
}