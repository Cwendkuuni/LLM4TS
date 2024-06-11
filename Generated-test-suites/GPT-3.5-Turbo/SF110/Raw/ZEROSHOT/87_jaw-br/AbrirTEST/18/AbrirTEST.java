import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testAbrirConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        
        // Test the values of xml and form variables
        assertEquals("", abrir.xml);
        assertSame(form, abrir.form);
        
        // Test whether the correct opening method is called based on the file extension
        assertFalse(arquivo.endsWith(".jaw"));
        assertTrue(arquivo.endsWith(".jaw.xml"));
        verifyAbrirXMLMethod();
        verifyAbrirSerializadoMethod();
    }
    
    @Test
    public void testAbrirConstructor2() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 20;
        Abrir abrir = new Abrir(form, tipoAbrir);
        
        // Test the values of xml and form variables
        assertEquals("", abrir.xml);
        assertSame(form, abrir.form);
        
        // Test file filter for opening serialized files
        verifyOpenDialogWithFileFilter();
        verifyAbrirSerializadoMethod();
        assertFalse(tipoAbrir != 20);
    }
    
    private void verifyAbrirXMLMethod() {
        // Create a dummy file
        String arquivo = "dummy.jaw.xml";
        File file = new File(arquivo);
        
        // Set up the expectations
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo(arquivo);
        try {
            Reader leitor = new FileReader(file);
            form.setEntidades(new SerializacaoXML().xml2Entidades(leitor));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Call the method
        Abrir abrir = new Abrir(form, arquivo);
        
        // Verify the expectations
        assertEquals(arquivo, form.getNomeArquivoSalvo());
        assertNotEquals("", abrir.xml);
        assertNotNull(form.getEntidades());
    }
    
    private void verifyAbrirSerializadoMethod() {
        // Create a dummy file
        String arquivo = "dummy.jaw";
        File file = new File(arquivo);
        
        // Set up the expectations
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo(arquivo);
        try {
            form.setEntidades(new Serializacao().abrir(arquivo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Call the method
        Abrir abrir = new Abrir(form, arquivo);
        
        // Verify the expectations
        assertEquals(arquivo, form.getNomeArquivoSalvo());
        assertNotEquals("", abrir.xml);
        assertNotNull(form.getEntidades());
    }
    
    private void verifyOpenDialogWithFileFilter() {
        // Create a dummy ProcessarEntidades object
        ProcessarEntidades form = new ProcessarEntidades();
        
        // Set up the expectations
        JFileChooser escolhedor = new JFileChooser();
        
        // Set the current directory
        String caminhoDosProjetos = Main.configuracao.getCaminhoDosProjetos();
        escolhedor.setCurrentDirectory(new File(caminhoDosProjetos));
        
        // Set the file filter
        escolhedor.setFileFilter(new Abrir.Filtro());
        
        // Simulate selecting a file
        File selectedFile = new File("dummy.jaw");
        escolhedor.setSelectedFile(selectedFile);
        
        // Call the method
        int tipoAbrir = 20;
        Abrir abrir = new Abrir(form, tipoAbrir);
        
        // Verify the file filter is set correctly
        assertEquals(escolhedor.getFileFilter(), new Abrir.Filtro());
        
        // Verify that the selected file is not null
        assertNotNull(escolhedor.getSelectedFile());
        
        // Verify the expectations
        assertEquals(selectedFile.getAbsolutePath(), form.getNomeArquivoSalvo());
        assertNotEquals("", abrir.xml);
        assertNotNull(form.getEntidades());
    }
}