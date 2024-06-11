import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Assert;
import org.junit.Test;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructorWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(0, population.getPopulationSize());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithNegativePopulationLimit() {
        new ListPopulation(-1);
    }

    @Test
    public void testListPopulationConstructorWithInitialChromosomes() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));

        ListPopulation population = new ListPopulation(initialChromosomes, 5);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertArrayEquals(initialChromosomes.get(0).getGenes(), population.getChromosomes().get(0).getGenes());
        Assert.assertArrayEquals(initialChromosomes.get(1).getGenes(), population.getChromosomes().get(1).getGenes());
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullInitialChromosomes() {
        new ListPopulation(null, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithInitialChromosomesExceedingPopulationLimit() {
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));

        ListPopulation population = new ListPopulation(initialChromosomes, 3);
    }

    @Test
    public void testSetChromosomesWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new Chromosome(new byte[10]));
        chromosomesToAdd.add(new Chromosome(new byte[10]));

        population.setChromosomes(chromosomesToAdd);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertArrayEquals(chromosomesToAdd.get(0).getGenes(), population.getChromosomes().get(0).getGenes());
        Assert.assertArrayEquals(chromosomesToAdd.get(1).getGenes(), population.getChromosomes().get(1).getGenes());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomesToAdd() {
        ListPopulation population = new ListPopulation(5);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithChromosomesToAddExceedingPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new Chromosome(new byte[10]));
        chromosomesToAdd.add(new Chromosome(new byte[10]));
        chromosomesToAdd.add(new Chromosome(new byte[10]));

        population.setChromosomes(chromosomesToAdd);
    }

    @Test
    public void testAddChromosomesWithPopulationLimitNotExceeded() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new Chromosome(new byte[10]));

        population.addChromosomes(chromosomesToAdd);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertArrayEquals(chromosomesToAdd.get(0).getGenes(), population.getChromosomes().get(1).getGenes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> chromosomesToAdd = new ArrayList<>();
        chromosomesToAdd.add(new Chromosome(new byte[10]));
        chromosomesToAdd.add(new Chromosome(new byte[10]));
        chromosomesToAdd.add(new Chromosome(new byte[10]));

        population.addChromosomes(chromosomesToAdd);
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome(new byte[10]));
        expectedChromosomes.add(new Chromosome(new byte[10]));

        population.addChromosomes(expectedChromosomes);
        List<Chromosome> actualChromosomes = population.getChromosomes();

        Assert.assertEquals(expectedChromosomes.size(), actualChromosomes.size());
        for (int i = 0; i < expectedChromosomes.size(); i++) {
            Assert.assertArrayEquals(expectedChromosomes.get(i).getGenes(), actualChromosomes.get(i).getGenes());
        }
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> expectedChromosomes = new ArrayList<>();
        expectedChromosomes.add(new Chromosome(new byte[10]));
        expectedChromosomes.add(new Chromosome(new byte[10]));

        population.addChromosomes(expectedChromosomes);
        List<Chromosome> actualChromosomes = population.getChromosomeList();

        Assert.assertEquals(expectedChromosomes.size(), actualChromosomes.size());
        for (int i = 0; i < expectedChromosomes.size(); i++) {
            Assert.assertArrayEquals(expectedChromosomes.get(i).getGenes(), actualChromosomes.get(i).getGenes());
        }
    }

    @Test
    public void testAddChromosomeWithPopulationLimitNotExceeded() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosomeToAdd = new Chromosome(new byte[10]);

        population.addChromosome(chromosomeToAdd);
        Assert.assertEquals(5, population.getPopulationLimit());
        Assert.assertEquals(3, population.getPopulationSize());
        Assert.assertArrayEquals(chromosomeToAdd.getGenes(), population.getChromosomes().get(2).getGenes());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithPopulationLimitExceeded() {
        ListPopulation population = new ListPopulation(5);
        Chromosome chromosomeToAdd = new Chromosome(new byte[10]);

        population.addChromosome(chromosomeToAdd);
    }

    @Test
    public void testGetFittestChromosome() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        Chromosome fittestChromosome = new Chromosome(new byte[10]);
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(fittestChromosome);
        initialChromosomes.add(new Chromosome(new byte[10]));

        population.setChromosomes(initialChromosomes);
        Chromosome actualFittestChromosome = population.getFittestChromosome();

        Assert.assertArrayEquals(fittestChromosome.getGenes(), actualFittestChromosome.getGenes());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        Assert.assertEquals(5, population.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimitWithPositivePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(-1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithNewPopulationSizeSmallerThanCurrentPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        population.setPopulationLimit(3);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));

        population.setChromosomes(initialChromosomes);
        Assert.assertEquals(3, population.getPopulationSize());
    }

    @Test
    public void testToString() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));

        population.setChromosomes(initialChromosomes);
        String actualString = population.toString();

        String expectedString = "[Chromosome[byte], Chromosome[byte], Chromosome[byte]]";
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(5);
        List<Chromosome> initialChromosomes = new ArrayList<>();
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));
        initialChromosomes.add(new Chromosome(new byte[10]));

        population.setChromosomes(initialChromosomes);
        Iterator<Chromosome> iterator = population.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            Chromosome nextChromosome = iterator.next();
            Assert.assertArrayEquals(initialChromosomes.get(index).getGenes(), nextChromosome.getGenes());
            index++;
        }
    }
}