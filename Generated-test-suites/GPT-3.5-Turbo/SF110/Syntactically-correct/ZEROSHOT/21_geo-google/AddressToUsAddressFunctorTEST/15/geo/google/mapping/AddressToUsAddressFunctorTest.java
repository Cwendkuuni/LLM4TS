import static org.junit.Assert.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecuteWhenAddressHasCountry() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        // Act
        GeoUsAddress result = functor.execute(addr);

        // Assert
        assertEquals("US", result.getCountry());
    }

    @Test
    public void testExecuteWhenAddressHasAdminArea() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        List<AdministrativeArea.AdministrativeAreaName> names = new ArrayList<>();
        AdministrativeArea.AdministrativeAreaName name = new AdministrativeArea.AdministrativeAreaName();
        name.setContent("California");
        names.add(name);
        adminArea.setAdministrativeAreaName(names);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        // Act
        GeoUsAddress result = functor.execute(addr);

        // Assert
        assertEquals("California", result.getState());
    }

    @Test
    public void testExecuteWhenAddressHasSubAdminArea() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AdministrativeArea adminArea = new AdministrativeArea();
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdminArea = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea();
        List<AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subNames = new ArrayList<>();
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subName.setContent("County");
        subNames.add(subName);
        subAdminArea.setSubAdministrativeAreaName(subNames);
        adminArea.setSubAdministrativeArea(subAdminArea);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);

        // Act
        GeoUsAddress result = functor.execute(addr);

        // Assert
        assertEquals("County", result.getCounty());
    }

    // More test methods for all other scenarios...

}