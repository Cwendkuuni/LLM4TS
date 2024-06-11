import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import javax.swing.*;
import java.io.*;
import jaw.metamodelo.*;
import jaw.ProcessarEntidades;
import jaw.Salvar;

public class SalvarTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
    }

    // Test case for the save file method with SALVAR_XML type
    @Test
    public void testSalvarXML() {
        // Creating a mock ProcessarEntidades instance
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> mockEntidades = new ArrayList<>();
        Entidade entidade1 = new Entidade();
        Entidade entidade2 = new Entidade();
        mockEntidades.add(entidade1);
        mockEntidades.add(entidade2);
        form.setEntidades(mockEntidades);

        // Creating a Salvar instance with SALVAR_XML type
        Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);

        // Checking if the XML file was created and saved successfully
        String expectedXml = "<xml>...</xml>"; // the expected XML content
        String actualXml = readFile(salvar.arquivo);
        assertEquals(expectedXml, actualXml);
    }

    // Test case for the save file method with SALVAR type
    @Test
    public void testSalvar() {
        // Creating a mock ProcessarEntidades instance
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> mockEntidades = new ArrayList<>();
        Entidade entidade1 = new Entidade();
        Entidade entidade2 = new Entidade();
        mockEntidades.add(entidade1);
        mockEntidades.add(entidade2);
        form.setEntidades(mockEntidades);

        // Creating a Salvar instance with SALVAR type
        Salvar salvar = new Salvar(form, Salvar.SALVAR);

        // Checking if the file was saved successfully
        boolean expected = true; // assuming the file was saved successfully
        boolean actual = checkFileExists(salvar.arquivo);
        assertEquals(expected, actual);
    }

    // Test case for the save file method with SALVAR_COMO type
    @Test
    public void testSalvarComo() {
        // Creating a mock ProcessarEntidades instance
        ProcessarEntidades form = new ProcessarEntidades();
        List<Entidade> mockEntidades = new ArrayList<>();
        Entidade entidade1 = new Entidade();
        Entidade entidade2 = new Entidade();
        mockEntidades.add(entidade1);
        mockEntidades.add(entidade2);
        form.setEntidades(mockEntidades);

        // Creating a Salvar instance with SALVAR_COMO type
        Salvar salvar = new Salvar(form, Salvar.SALVAR_COMO);

        // Checking if the file was saved successfully
        boolean expected = true; // assuming the file was saved successfully
        boolean actual = checkFileExists(salvar.arquivo);
        assertEquals(expected, actual);
    }

    // Helper method to check if a file exists
    private boolean checkFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }

    // Helper method to read the contents of a file
    private String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}