import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import jaw.metamodelo.*;
import jaw.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Vector;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setUp() throws Exception {
        form = mock(ProcessarEntidades.class);
        when(form.getNomeArquivoSalvo()).thenReturn("Novo Projeto");
        when(form.getName()).thenReturn("");
        when(form.getEntidades()).thenReturn(new Vector<Entidade>());
        Main.configuracao = mock(Configuracao.class);
        when(Main.configuracao.getCaminhoDosProjetos()).thenReturn("");
        salvar = new Salvar(form, Salvar.SALVAR);
    }

    @Test
    public void testSalvar() throws Exception {
        assertEquals("Novo Projeto.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarWithExistingFile() throws Exception {
        when(form.getNomeArquivoSalvo()).thenReturn("ExistingFile");
        salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals("ExistingFile.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarXml() throws Exception {
        when(form.getNomeArquivoSalvo()).thenReturn("Novo Projeto");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertEquals("Novo Projeto.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarComO() throws Exception {
        when(form.getNomeArquivoSalvo()).thenReturn("Novo Projeto");
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertEquals("Novo Projeto.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarWithFileNameAndType() throws Exception {
        when(form.getNomeArquivoSalvo()).thenReturn("SampleFile");
        salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals("SampleFile.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarXmlWithFileNameAndType() throws Exception {
        when(form.getNomeArquivoSalvo()).thenReturn("SampleFile");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertEquals("SampleFile.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarComOWithFileNameAndType() throws Exception {
        when(form.getNomeArquivoSalvo()).thenReturn("SampleFile");
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertEquals("SampleFile.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarWithEmptyFile() throws Exception {
        when(form.getName()).thenReturn("EmptyFile");
        salvar = new Salvar(form, Salvar.SALVAR);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarXmlWithEmptyFile() throws Exception {
        when(form.getName()).thenReturn("EmptyFile");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarComOWithEmptyFile() throws Exception {
        when(form.getName()).thenReturn("EmptyFile");
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarWithNoSelectedFile() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        salvar = new Salvar(form, Salvar.SALVAR);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarXmlWithNoSelectedFile() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarComOWithNoSelectedFile() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarWithNoSelectedFileAndEmptyName() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        when(form.getName()).thenReturn("");
        salvar = new Salvar(form, Salvar.SALVAR);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarXmlWithNoSelectedFileAndEmptyName() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        when(form.getName()).thenReturn("");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarComOWithNoSelectedFileAndEmptyName() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        when(form.getName()).thenReturn("");
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarWithNoSelectedFileAndExistingName() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        when(form.getNomeArquivoSalvo()).thenReturn("ExistingFile");
        salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals("ExistingFile.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarXmlWithNoSelectedFileAndExistingName() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        when(form.getNomeArquivoSalvo()).thenReturn("ExistingFile");
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertEquals("ExistingFile.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarComOWithNoSelectedFileAndExistingName() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        when(escolhedor.getSelectedFile()).thenReturn(null);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        when(form.getNomeArquivoSalvo()).thenReturn("ExistingFile");
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertEquals("ExistingFile.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarWithSelectedFile() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("SelectedFile");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals("SelectedFile", salvar.arquivo);
    }

    @Test
    public void testSalvarXmlWithSelectedFile() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("SelectedFile");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertEquals("SelectedFile.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarComOWithSelectedFile() throws Exception {
        JFileChooser escolhedor = mock(JFileChooser.class);
        File selectedFile = mock(File.class);
        when(selectedFile.getAbsolutePath()).thenReturn("SelectedFile");
        when(escolhedor.getSelectedFile()).thenReturn(selectedFile);
        whenNew(JFileChooser.class).withNoArguments().thenReturn(escolhedor);
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertEquals("SelectedFile.jaw", salvar.arquivo);
    }

}